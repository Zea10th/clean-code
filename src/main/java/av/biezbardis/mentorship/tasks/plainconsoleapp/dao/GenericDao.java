package av.biezbardis.mentorship.tasks.plainconsoleapp.dao;

import java.util.List;
import java.util.Optional;

/**
 * Classes that implement this interface provides key operations (CRUD) with records in the database.
 * save(T t) - adds new record to the database
 * findById(int id) - returns a record from the database
 * findAll() - returns all records from the database
 * update(T t) - updates the record
 * delete(int id) - deletes the record
 *
 * @param <T> type of records
 */
public interface GenericDao<T> {
    /**
     * Creates a new entry in the database table
     *
     * @param t entry type, which determines which table the entry will be entered into
     */
    void save(T t);

    /**
     * Returns optional object that may contain a record in the database if it was stored, if not object will be empty
     *
     * @param id record that is possibly stored in the database
     * @return optional of type
     */
    Optional<T> findById(Long id);

    /**
     * Returns all records of provided type from the database
     *
     * @return list of all records of T type
     */
    List<T> findAll();

    /**
     * Updates attributes of a record in the database
     *
     * @param t updated record for subsequent replacement in the database
     */
    void update(T t);

    /**
     * Deletes a record with provided Id from the database
     *
     * @param id of the record to be deleted
     */
    void delete(Long id);
}
