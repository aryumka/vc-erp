package aryumka.vcerp.api.fundMng

import aryumka.vcerp.api.fundMng.contoller.ProposeController
import aryumka.vcerp.api.fundMng.model.FundingProposal
import aryumka.vcerp.api.fundMng.repository.ProposalRepository
import aryumka.vcerp.api.fundMng.service.ProposeService
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

class ProposeGenerator {
    fun generateProposal(title: String): FundingProposal =
        FundingProposal(title = title)
}

class ProPoseGeneratorTest: BehaviorSpec({
    given("제안서 생성기가 주어졌을 때") {
        val generator = ProposeGenerator()

        `when`("제안서를 생성한다면") {
            val proposal = generator.generateProposal("제안서 제목")

            then("제안서는 생성된다") {
                proposal.title shouldBe "제안서 제목"
            }
        }
    }
})

@DataJpaTest
class ProposalRepositoryTest(@Autowired private val proposalRepository: ProposalRepository) : BehaviorSpec() {
    init {
        given("A proposal repository") {
            `when`("a proposal is saved") {
                val proposal = proposalRepository.save(FundingProposal(title = "Sample Proposal"))

                then("the proposal should be retrievable") {
                    val retrievedProposal = proposalRepository.findById(proposal.id).orElse(null)
                    retrievedProposal shouldNotBe null
                    retrievedProposal.title shouldBe "Sample Proposal"
                }
            }
        }
    }
}

@SpringBootTest
class ProposeUpdateTest(@Autowired private val repository: ProposalRepository): FunSpec() {
//    @Autowired //이런식으로는 사용할 수 없다
//    private val repository: ProposalRepository
    @Autowired
    val service = ProposeService(repository)

    init {
        context("신청된 제안서가 있는 상황에서") {
            val proposal = service.createFundingProposal("제안서 제목")
            println("proposal: $proposal")

            test("제안서를 수정한다면") {
                val newTitle = "새로운 제안서 제목"
//                    retrievedProposal?.title shouldBe "Sample Proposal"
                val updatedProposal = service.updateFundingProposal(proposal.id, newTitle)
                println("updatedProposal: $updatedProposal")
                updatedProposal shouldNotBe null
                updatedProposal?.title shouldBe newTitle
            }
        }
    }
}

@WebMvcTest
@AutoConfigureMockMvc
class ProposeUpdateTestWithMockk(@Autowired val mockMvc: MockMvc): FunSpec() {
    @MockkBean
    private lateinit var service: ProposeService
    init {
        beforeTest{
            service = mockk()
            every { service.createFundingProposal(any()) } returns FundingProposal(1L, "제안서 제목")
            every { service.updateFundingProposal(any(), any()) } returns FundingProposal(1L, "새로운 제안서 제목")

        }
        context("신청된 제안서가 있는 상황에서") {
            mockMvc.perform(post("/fundMng/proposals/create").content("제안서 제목").contentType("text/plain"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("제안서 제목"))

            test("제안서를 수정한다면") {
                mockMvc.perform(put("/fundMng/proposals/1").content("새로운 제안서 제목").contentType("text/plain"))
                    .andExpect(status().isOk)
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.title").value("새로운 제안서 제목"))
            }
        }
    }
}


@WebMvcTest(ProposeController::class)
@AutoConfigureMockMvc
class ProposeUpdateTestGpt(@Autowired val mockMvc: MockMvc) : FunSpec() {

    @MockkBean
    private lateinit var proposalRepository: ProposalRepository

    @MockkBean
    private lateinit var service: ProposeService

    init {
        test("제안서 생성") {
            val expectedId = 1L
            val expectedTitle = "제안서 제목"

            proposalRepository = mockk()
            service = mockk()
//            every { service.createFundingProposal(any()) } returns FundingProposal(1L, "제안서 제목")
//            every { proposalRepository.findById(any()) } returns Optional.of(FundingProposal(1L, "제안서 제목"))
            every { service.getFundingProposalById(1) } returns FundingProposal(1L, "제안서 제목")

            mockMvc
                .perform(
                    get("/fundMng/proposal/1")
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(expectedId))
                .andExpect(jsonPath("$.title").value(expectedTitle))

            verify { service.createFundingProposal(any()) }
        }

        test("제안서 수정") {
            val proposalId = 1
            val newTitle = "새로운 제안서 제목"
            val expectedId = 1L
            val expectedTitle = "새로운 제안서 제목"

            mockMvc.perform(put("/fundMng/proposal/1")
                .content(newTitle)
                .contentType("text/plain")
            ).andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(expectedId))
                .andExpect(jsonPath("$.title").value(expectedTitle))
        }
    }
}
