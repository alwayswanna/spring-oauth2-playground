package a.gleb.oauth2server.service

import a.gleb.oauth2server.db.repository.AccountRepository
import a.gleb.oauth2server.db.repository.RoleRepository
import a.gleb.oauth2server.exception.BadRequestException
import a.gleb.oauth2server.exception.RoleNotFoundException
import a.gleb.oauth2server.exception.UserNotFoundException
import a.gleb.oauth2server.logger
import a.gleb.oauth2server.mapper.AccountMapper
import a.gleb.oauth2server.model.AccountRequest
import a.gleb.oauth2server.model.AccountResponse
import a.gleb.oauth2server.model.AccountSearchRequest
import org.springframework.stereotype.Service
import java.util.*


@Service
class AccountService(
    private val accountMapper: AccountMapper,
    private val roleRepository: RoleRepository,
    private val accountRepository: AccountRepository
) {

    /**
     * Create new account.
     */
    fun create(request: AccountRequest): AccountResponse {
        logger.info { "Create new account with login=${request.login}" }
        val account = accountMapper.toEntity(request)
        account.apply {
            roles = roleRepository.findRolesByIdIn(request.roleIds)
                ?: throw RoleNotFoundException("Role search returned null. [ids=${request.roleIds.toTypedArray()}]")
        }

        val savedAccount = accountRepository.save(account)
        return accountMapper.toResponse(savedAccount)
    }

    /**
     * Delete account by id.
     */
    fun delete(id: UUID) {
        logger.info { "Delete account with id=$id" }
        accountRepository.deleteById(id)
        logger.info { "Successfully delete role with id=$id" }
    }

    /**
     * Get all accounts.
     */
    fun getAccounts(): List<AccountResponse> {
        logger.info { "Search all accounts." }
        return accountRepository.findAll()
            .map { accountMapper.toResponse(it) }
            .toList()
    }

    /**
     * Update account
     */
    fun update(request: AccountRequest): AccountResponse {
        logger.info { "Attempt to update account with id=${request.id}" }
        if (request.id == null) {
            throw BadRequestException("Can`t change account when request.id is NULL")
        }

        accountRepository.findById(request.id)
            .orElseThrow { UserNotFoundException("Account with id=${request.id} doest not exists") }
        val entityToSave = accountMapper.toEntity(request)
        val roles = roleRepository.findRolesByIdIn(request.roleIds)
        entityToSave.roles = roles ?: emptyList()
        entityToSave.id = request.id

        return accountMapper.toResponse(accountRepository.save(entityToSave))
    }

    /**
     * Search account by filter
     * TODO(change realization)
     */
    fun searchAccount(request: AccountSearchRequest): AccountResponse {
        logger.info { "Search account by filter $request" }
        val account = if (request.id != null) {
            accountRepository.findById(request.id).orElse(null)
        } else {
            accountRepository.findAccountByPreferredUsername(request.login!!)
        }

        account ?: throw UserNotFoundException("Account with filter=$request doest not exists")

        return accountMapper.toResponse(account)
    }
}