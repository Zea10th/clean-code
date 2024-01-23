package av.biezbardis.mentorship.tasks.consoleapp.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface for providing key (CRUD) operations with records in the database
 * save(T t) - adds new record to the database
 * get(int id) - returns a record from the database
 * getAll() - returns all records from the database
 * update(T t) - updates the record
 * delete(int id) - deletes the record
 *
 * @param <T> type of records
 */
public interface Dao<T> {
    /**
     * Creates a new entry in the database table
     *
     * @param t entry type, which determines which table the entry will be entered into
     * @return entity's Id from the table as successful result or -1 if not
     */
    int save(T t);

    /**
     * Returns optional object that may contain a record in the database if it was stored, if not object will be empty
     *
     * @param id record that is possibly stored in the database
     * @return optional of type
     */
    Optional<T> get(int id);

    /**
     * Returns all records of provided type from the database
     *
     * @return list of all records of T type
     */
    List<T> getAll();

    /**
     * Updates attributes of a record in the database
     *
     * @param t updated record for subsequent replacement in the database
     * @return true if successful and false otherwise
     */
    boolean update(T t);

    /**
     * Deletes a record with provided Id from the database
     *
     * @param id of the record to be deleted
     * @return true if successful and false otherwise
     */
    boolean delete(int id);
}