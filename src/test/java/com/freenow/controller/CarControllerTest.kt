package com.freenow.controller

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.freenow.datatransferobject.CarDTO
import com.freenow.util.EngineType
import com.freenow.util.ManufacturerName
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*


private const val BASE_URL = "/v1/cars"

@SpringBootTest
@AutoConfigureMockMvc
internal class CarControllerTest @Autowired constructor(
    var mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    @Nested
    @DisplayName("GET ${BASE_URL}/all")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @WithMockUser(username = "daniel")
    inner class GetAllCars {

        @Test
        fun `should return all cars`() {
            mockMvc.get("$BASE_URL/all")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("cars.length()") { value(6) }
                }
        }
    }

    @Nested
    @DisplayName("GET $BASE_URL/{carId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @WithMockUser(username = "daniel")
    inner class GetCar {

        @Test
        fun `should return the car with the given id`() {

            // given
            val carId = 3L
            val car = CarDTO.newBuilder()
                .setId(carId)
                .setDeleted(false)
                .setLicensePlate("HH AS 2283")
                .setSeatCount(3)
                .setConvertible(true)
                .setRating(4.0)
                .setManufacturerName(ManufacturerName.RENAULT)
                .setEngineType(EngineType.GAS)
                .createCarDTO()

            // when / then
            mockMvc.get("$BASE_URL/$carId")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                            .writeValueAsString(car)
                    }
                }
        }

        @Test
        fun `should return NOT FOUND if the car with the given id does not exist`() {

            // given
            val carId = 0

            // when / then
            mockMvc.get("$BASE_URL/$carId")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }

    }

    @Nested
    @DisplayName("POST $BASE_URL")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @WithMockUser(username = "daniel")
    inner class PostNewCar {

        @Test
        @DirtiesContext
        fun `should add a new car`() {
            // given
            val newCar = CarDTO.newBuilder()
                .setId(7L)
                .setDeleted(false)
                .setLicensePlate("Special licence")
                .setSeatCount(5)
                .setConvertible(false)
                .setRating(0.0)
                .setManufacturerName(ManufacturerName.MERCEDES)
                .setEngineType(EngineType.ELECTRIC)
                .createCarDTO()

            // when
            val performPost = mockMvc.post(BASE_URL) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newCar)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(newCar))
                    }
                }

            mockMvc.get("$BASE_URL/${newCar.id}")
                .andExpect { content { json(objectMapper.writeValueAsString(newCar)) } }

        }


        @Test
        fun `should return BAD REQUEST when constraints are not satisfied`() {
            // given
            val newCar = CarDTO.newBuilder()
                .setId(7L)
                .setLicensePlate("agad")
                .setSeatCount(10)
                .setConvertible(false)
                .setRating(-1.0)
                .setManufacturerName(ManufacturerName.MERCEDES)
                .setEngineType(EngineType.ELECTRIC)
                .createCarDTO()

            // when
            val performPost = mockMvc.post(BASE_URL) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newCar)
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
    @DisplayName("PATCH $BASE_URL/{carId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @WithMockUser(username = "daniel")
    inner class PatchExistingCar {

        @Test
        fun `should update an existing car`() {
            // given
            val carId = 5L
            val newRating = 4.6

            val newCar = CarDTO.newBuilder()
                .setId(carId)
                .setDeleted(false)
                .setLicensePlate("HH AS 9883")
                .setSeatCount(7)
                .setConvertible(true)
                .setRating(newRating)
                .setManufacturerName(ManufacturerName.MERCEDES)
                .setEngineType(EngineType.GAS)
                .createCarDTO()

            // when
            val performPatch = mockMvc.patch("$BASE_URL/${carId}?carRating=$newRating")

            // then
            performPatch
                .andDo { print() }
                .andExpect { status { isOk() } }

            mockMvc.get("$BASE_URL/${newCar.id}")
                .andExpect { content { json(objectMapper.writeValueAsString(newCar)) } }
        }


        @Test
        fun `should return NOT FOUND if car with given id was not found`() {
            // given
            val carId = 0L

            val invalidCar = CarDTO.newBuilder()
                .setId(carId)
                .setDeleted(false)
                .setLicensePlate("HH AS 9883")
                .setSeatCount(7)
                .setConvertible(true)
                .setRating(1.0)
                .setManufacturerName(ManufacturerName.MERCEDES)
                .setEngineType(EngineType.GAS)
                .createCarDTO()

            // when
            val performPatchRequest = mockMvc.patch("$BASE_URL/${invalidCar.id}?carRating=4.0") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidCar)
            }

            // then
            performPatchRequest
                .andDo { print() }
                .andExpect { status { isNotFound() } }

        }

    }

    @Nested
    @DisplayName("DELETE $BASE_URL/{carId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @WithMockUser(username = "daniel")
    inner class DeleteExistingCar {

        @Test
        @DirtiesContext
        fun `should set field deleted to true for the car with given id`() {
            // given
            val carId = 1L

            val expectedCar = CarDTO.newBuilder()
                .setId(carId)
                .setDeleted(true)
                .setLicensePlate("HH AA 6574")
                .setSeatCount(5)
                .setConvertible(false)
                .setRating(4.5)
                .setManufacturerName(ManufacturerName.BMW)
                .setEngineType(EngineType.DIESEL)
                .createCarDTO()

            // when / then
            mockMvc.delete("$BASE_URL/$carId")
                .andDo { print() }

            mockMvc.get("$BASE_URL/$carId")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { objectMapper.writeValueAsString(expectedCar) }
                }
        }

        @Test
        fun `should return NOT FOUND if the car with the given id does not exist`() {

            val carId = 0

            // when / then
            mockMvc.delete("$BASE_URL/$carId")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }

    }
}