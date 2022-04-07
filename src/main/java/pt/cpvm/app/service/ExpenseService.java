package pt.cpvm.app.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.cpvm.app.domain.Expense;
import pt.cpvm.app.repository.ExpenseRepository;
import pt.cpvm.app.service.dto.ExpenseDTO;
import pt.cpvm.app.service.mapper.ExpenseMapper;

/**
 * Service Implementation for managing {@link Expense}.
 */
@Service
@Transactional
public class ExpenseService {

    private final Logger log = LoggerFactory.getLogger(ExpenseService.class);

    private final ExpenseRepository expenseRepository;

    private final ExpenseMapper expenseMapper;

    public ExpenseService(ExpenseRepository expenseRepository, ExpenseMapper expenseMapper) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
    }

    /**
     * Save a expense.
     *
     * @param expenseDTO the entity to save.
     * @return the persisted entity.
     */
    public ExpenseDTO save(ExpenseDTO expenseDTO) {
        log.debug("Request to save Expense : {}", expenseDTO);
        Expense expense = expenseMapper.toEntity(expenseDTO);
        expense = expenseRepository.save(expense);
        return expenseMapper.toDto(expense);
    }

    /**
     * Update a expense.
     *
     * @param expenseDTO the entity to save.
     * @return the persisted entity.
     */
    public ExpenseDTO update(ExpenseDTO expenseDTO) {
        log.debug("Request to save Expense : {}", expenseDTO);
        Expense expense = expenseMapper.toEntity(expenseDTO);
        expense = expenseRepository.save(expense);
        return expenseMapper.toDto(expense);
    }

    /**
     * Partially update a expense.
     *
     * @param expenseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExpenseDTO> partialUpdate(ExpenseDTO expenseDTO) {
        log.debug("Request to partially update Expense : {}", expenseDTO);

        return expenseRepository
            .findById(expenseDTO.getId())
            .map(existingExpense -> {
                expenseMapper.partialUpdate(existingExpense, expenseDTO);

                return existingExpense;
            })
            .map(expenseRepository::save)
            .map(expenseMapper::toDto);
    }

    /**
     * Get all the expenses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Expenses");
        return expenseRepository.findAll(pageable).map(expenseMapper::toDto);
    }

    /**
     * Get one expense by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExpenseDTO> findOne(Long id) {
        log.debug("Request to get Expense : {}", id);
        return expenseRepository.findById(id).map(expenseMapper::toDto);
    }

    /**
     * Delete the expense by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Expense : {}", id);
        expenseRepository.deleteById(id);
    }
}
