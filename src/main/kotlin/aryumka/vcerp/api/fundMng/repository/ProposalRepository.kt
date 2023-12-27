package aryumka.vcerp.api.fundMng.repository

import aryumka.vcerp.api.fundMng.model.FundingProposal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProposalRepository : JpaRepository<FundingProposal, Long> {
}