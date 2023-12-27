package aryumka.vcerp.api.fundMng.service

import aryumka.vcerp.api.fundMng.model.FundingProposal
import aryumka.vcerp.api.fundMng.repository.ProposalRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ProposeService(
    private val repository: ProposalRepository
) {
    @Transactional
    fun createFundingProposal(title: String): FundingProposal =
        repository.save(FundingProposal(title = title))
    @Transactional
    fun getFundingProposalById(id: Long): FundingProposal? =
        repository.findById(id).orElse(null)

    @Transactional
    fun getAllFundingProposals(): List<FundingProposal> =
        repository.findAll()

    @Transactional
    fun updateFundingProposal(id: Long, newTitle: String): FundingProposal? =
        repository.findById(id).map {
            it.copy(title = newTitle)
        }.orElse(null)

    @Transactional
    fun deleteFundingProposal(id: Long) {
        repository.deleteById(id)
    }
}