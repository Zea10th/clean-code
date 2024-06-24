package av.biezbardis.mentorship.tasks.plainconsoleapp.service;

import java.util.List;

/**
 * Interface for providing key (CRUD) operations with stored records
 * save(String[] args) - adds new record to the storage using provided args
 * get(int id) - returns a record from the storage
 * getAll() - returns all records from the storage
 * update(String[] args) - updates the stored record using provided args
 * delete(int id) - deletes the stored record
 *
 * @param <T> type of records
 */
public interface Service<T> {
    /**
     * Validates provided arguments and try to create a new entry in the storage
     *
     * @param t as a record to save in the storage
     */
    void save(T t);

    /**
     * Returns the stored record if it exists
     *
     * @param id record that is possibly stored
     * @return record attributes as a successful action or a message with failure if not
     */
    T findById(long id);

    /**
     * Returns all stored records of provided type
     *
     * @return list of all records of T type
     */
    List<T> findAll();

    /**
     * Validates provided argument and try to update attributes of the stored record
     *
     * @param t updated record for subsequent replacement in the storage
     */
    void update(T t);

    /**
     * Deletes a record with provided Id if it was stored
     *
     * @param id of the record to be deleted
     */
    void delete(long id);
}
