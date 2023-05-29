package me.dio.credit.application.system.controller

import me.dio.credit.application.system.dto.CreditDto
import me.dio.credit.application.system.dto.CreditView
import me.dio.credit.application.system.dto.CreditViewlist
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.service.impl.CreditService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.stream.Collector
import java.util.stream.Collectors


@RestController
@RequestMapping("/api/credits")
class CreditResource(
    private val creditService: CreditService

) {
    @PostMapping
    fun saveCredit(@RequestParam creditDto: CreditDto): ResponseEntity<String> {
        val credit: Credit = this.creditService.save(creditDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Credit ${credit.creditCode}- Custumer ${credit.customer?.firstName} saved!")

    }


    @GetMapping
    fun findAllCustomerId(@RequestParam(value = "customerId") customerId: Long): ResponseEntity<List<CreditViewlist>> {
        val creditViewlist: List<CreditViewlist> = this.creditService.findAllByCustomer(customerId).stream()
            .map { credit: Credit -> CreditViewlist(credit) }.collect(Collectors.toList())
        return ResponseEntity.status(HttpStatus.OK).body(creditViewlist)
    }

    @GetMapping("/{creditCode}")
    fun findBydCreditCode(
        @RequestParam(value = "customerId") customerId: Long,
        @PathVariable creditCode: UUID
    ): ResponseEntity<CreditView> {
        val credit: Credit = this.creditService.findByCreditCode(customerId, creditCode)
        return ResponseEntity.status(HttpStatus.OK).body(CreditView(credit))

    }


}