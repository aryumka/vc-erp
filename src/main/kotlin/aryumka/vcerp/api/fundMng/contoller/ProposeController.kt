package aryumka.vcerp.api.fundMng.contoller

import aryumka.vcerp.api.fundMng.model.FundingProposal
import aryumka.vcerp.api.fundMng.service.ProposeService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController("/fundMng/proposals")
internal class ProposeController(
    private val service: ProposeService
) {
    @GetMapping("/{id}")
    fun getProposalById(@PathVariable id: Long): FundingProposal? =
        runCatching{
            this.service.getFundingProposalById(id)
        }.getOrElse {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

    //todo 나중에 title은 dto로 받아야함
    @PostMapping("/create")
    fun createProposal(@RequestBody title: String): FundingProposal =
        this.service.createFundingProposal(title)

    //todo 나중에 title은 dto로 받아야함
    @PutMapping("/{id}")
    fun updateProposal(@PathVariable id: Long, @RequestBody newTitle: String): FundingProposal? =
        runCatching {
            this.service.updateFundingProposal(id, newTitle)
        }.getOrElse {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

    @DeleteMapping("/{id}")
    fun deleteProposal(@PathVariable id: Long) {
        this.service.deleteFundingProposal(id)
    }
}
