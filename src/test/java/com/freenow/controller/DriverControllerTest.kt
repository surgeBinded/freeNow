package com.freenow.controller

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.freenow.datatransferobject.CarDTO
import com.freenow.datatransferobject.DriverDTO
import com.freenow.datatransferobject.search.Rating
import com.freenow.datatransferobject.search.SearchFilters
import com.freenow.datatransferobject.search.SeatCount
import com.freenow.domainvalue.OnlineStatus
import com.freenow.util.EngineType
import com.freenow.util.ManufacturerName
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*
import org.springframework.transaction.annotation.Transactional

private const val BASE_URL = "/v1/drivers"

@SpringBootTest
@AutoConfigureMockMvc
class DriverControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    @Nested
    @DisplayName("GET $BASE_URL/all")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAllExistentDrivers {

        @Test
        @WithMockUser(username = "daniel")
        fun `should return all existing drivers`() {
            mockMvc.get("$BASE_URL/all")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("drivers.length()") { value(8) }
                    jsonPath("pageNo") { value(0) }
                }
        }
    }

    @Nested
    @DisplayName("GET $BASE_URL/{driverId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetCar {

        @Test
        @WithMockUser(username = "daniel")
        fun `should return driver with given id`() {
            // given
            val driverId = 3
            val driver = DriverDTO.newBuilder()
                .setId(3)
                .setUsername("driver03")
                .setPassword("driver03pw")
                .setCarDTO(setOf())

            // when / then
            mockMvc.get("$BASE_URL/$driverId")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                            .writeValueAsString(driver)
                    }
                }
        }

        @Test
        @WithMockUser(username = "daniel")
        fun `should return NOT FOUND if the driver with the given id does not exist`() {
            // given
            val driverId = 0

            // when / then
            mockMvc.get("$BASE_URL/$driverId")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("POST $BASE_URL")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostNewDriver {

        @Test
        @WithMockUser(username = "daniel")
        fun `should add new driver`() {
            // given
            val driver = DriverDTO.newBuilder()
                .setId(9)
                .setUsername("driver09")
                .setPassword("driver09pw")
                .setCarDTO(setOf())
                .createDriverDTO()

            // when
            val performPost = mockMvc.post(BASE_URL) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(driver)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(driver))
                    }
                }

            mockMvc.get("$BASE_URL/${driver.id}")
                .andExpect { content { json(objectMapper.writeValueAsString(driver)) } }
        }

        @Test
        @WithMockUser(username = "daniel")
        fun `should return BAD REQUEST when driver already exists`() {
            // given
            val driver = DriverDTO.newBuilder()
                .setId(9)
                .setUsername("driver03")
                .setPassword("driver03pw")
                .setCarDTO(setOf())
                .createDriverDTO()

            // when
            val performPost = mockMvc.post(BASE_URL) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(driver)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }

        @Test
        @WithMockUser(username = "daniel")
        fun `should return BAD REQUEST when constraints are not satisfied`() {
            // given
            val driver = DriverDTO.newBuilder()
                .setId(9)
                .setUsername("")
                .setPassword("")
                .setCarDTO(setOf())
                .createDriverDTO()

            // when
            val performPost = mockMvc.post(BASE_URL) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(driver)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    @DisplayName("DELETE $BASE_URL/{carId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteExistingCar {

        @Test
        @DirtiesContext
        @WithMockUser(username = "daniel")
        fun `should set field deleted to true for the driver with given id`() {
            // given
            val driverId = 1L

            val expectedDriver = DriverDTO.newBuilder()
                .setId(9)
                .setUsername("")
                .setPassword("")
                .setCarDTO(setOf())
                .createDriverDTO()

            // when
            mockMvc.delete("$BASE_URL/$driverId")
                .andDo { print() }

            // then
            mockMvc.get("$BASE_URL/$driverId")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { objectMapper.writeValueAsString(expectedDriver) }
                }
        }

        @Test
        @WithMockUser(username = "daniel")
        fun `should return NOT FOUND if the driver with the given id does not exist`() {

            // given
            val driverId = 0

            // when / then
            mockMvc.delete("$BASE_URL/$driverId")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("PATCH $BASE_URL/assignCar&unassignCar/{driverId}/{carId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    open inner class PatchExistingDriver {

        @Test
        @WithMockUser(username = "daniel")
        fun `should assign car with provided id to driver with provided id `() {
            // given
            val carId = 4
            val driverId = 3
            val expectedCar = CarDTO.newBuilder()
                .setId(4)
                .setDeleted(false)
                .setLicensePlate("HH AS 3283")
                .setSeatCount(2)
                .setConvertible(false)
                .setRating(4.9)
                .setManufacturerName(ManufacturerName.BMW)
                .setEngineType(EngineType.GAS)
                .createCarDTO()

            val expectedDriver = DriverDTO.newBuilder()
                .setId(3)
                .setUsername("driver03")
                .setPassword("driver03pw")
                .setCarDTO(mutableSetOf(expectedCar))
                .createDriverDTO()

            // when
            mockMvc.patch("$BASE_URL/assignCar/${driverId}/${carId}")
                .andDo { print() }
                .andExpect { status { isOk() } }

            // then
            mockMvc.get("$BASE_URL/$driverId")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { objectMapper.writeValueAsString(expectedDriver) }
                }
        }

        @Test
        @WithMockUser(username = "daniel")
        fun `should return BAD REQUEST if car is already assigned to some driver`() {
            // given
            val carId = 2
            val driverId = 2

            // when / then
            mockMvc.patch("$BASE_URL/assignCar/${driverId}/${carId}")
                .andDo { print() }
                .andExpect { status { isBadRequest() } }
        }

        @Test
        @WithMockUser(username = "daniel")
        fun `should return BAD REQUEST if when assigning car to deleted driver`() {
            // given
            val carId = 2
            val deletedDriverId = 5

            // when / then
            mockMvc.patch("$BASE_URL/assignCar/${deletedDriverId}/${carId}")
                .andDo { print() }
                .andExpect { status { isBadRequest() } }
        }

        @Test
        @WithMockUser(username = "daniel")
        fun `should return NOT FOUND if when driver not found`() {
            // given
            val carId = 2
            val unExitingDriverId = 0

            // when / then
            mockMvc.patch("$BASE_URL/assignCar/${unExitingDriverId}/${carId}")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }

        @Test
        @WithMockUser(username = "daniel")
        fun `should return NOT FOUND if when car not found`() {
            // given
            val carId = 0
            val unExitingDriverId = 5

            // when / then
            mockMvc.patch("$BASE_URL/assignCar/${unExitingDriverId}/${carId}")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }

        @Test
        @Transactional
        @WithMockUser(username = "daniel")
        open fun `should unassign car with provided id from driver with provided id `() {
            // given
            val carId = 2
            val driverId = 2

            val expectedDriver = DriverDTO.newBuilder()
                .setId(2)
                .setUsername("driver02")
                .setPassword("driver02pw")
                .setCarDTO(mutableSetOf())
                .createDriverDTO()

            // when
            mockMvc.patch("$BASE_URL/unassignCar/${driverId}/${carId}")
                .andDo { print() }
                .andExpect { status { isOk() } }

            // then
            mockMvc.get("$BASE_URL/$driverId")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { objectMapper.writeValueAsString(expectedDriver) }
                }
        }

        @Test
        @WithMockUser(username = "daniel")
        open fun `should return BAD REQUEST when unassigning car that is not assigned to driver with id`() {
            // given
            val carId = 1
            val driverId = 2

            // when / then
            mockMvc.patch("$BASE_URL/unassignCar/${driverId}/${carId}")
                .andDo { print() }
                .andExpect { status { isBadRequest() } }
        }

        @Test
        @WithMockUser(username = "daniel")
        fun `should return NOT FOUND when driver not found`() {
            // given
            val carId = 2
            val unExitingDriverId = 0

            // when / then
            mockMvc.patch("$BASE_URL/unassignCar/${unExitingDriverId}/${carId}")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }

        @Test
        @WithMockUser(username = "daniel")
        fun `should return NOT FOUND when car not found`() {
            // given
            val carId = 0
            val unExitingDriverId = 5

            // when / then
            mockMvc.patch("$BASE_URL/unassignCar/${unExitingDriverId}/${carId}")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("POST $BASE_URL/search")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetSearchResults {
        @Test
        @WithMockUser(username = "daniel")
        fun `should return drivers that satisfy the search filters`() {
            // given
            val searchFilters = SearchFilters(
                false,
                OnlineStatus.OFFLINE,
                false,
                ManufacturerName.RENAULT,
                EngineType.GAS,
                SeatCount(1, 5),
                Rating(3.0, 5.0),
                null
            )

            // when
            val performPost = mockMvc.post("$BASE_URL/search") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(searchFilters)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("drivers.length()") {value(2)}
                        jsonPath("totalElements") {value(2)}
                    }
                }
        }
    }
}