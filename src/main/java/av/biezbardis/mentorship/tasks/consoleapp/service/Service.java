package av.biezbardis.mentorship.tasks.consoleapp.service;

/**
 * Interface for providing key (CRUD) operations with stored records
 * save(String[] args) - adds new record to the storage using provided args
 * get(int id) - returns a record from the storage
 * getAll() - returns all records from the storage
 * update(String[] args) - updates the stored record using provided args
 * delete(int id) - deletes the stored record
 */
public interface Service {
    /**
     * Validates provided arguments and try to create a new entry in the storage
     * @param args with attributes of a provided entry
     * @return a (un)successful action message
     */
    String save(String[] args);

    /**
     * Returns the stored record if it exists
     * @param id record that is possibly stored
     * @return record attributes as a successful action or a message with failure if not
     */
    String get(int id);

    /**
     * Returns all stored records of provided type
     * @return list of all records of T type
     */
    String getAll();

    /**
     * Validates provided argument and try to update attributes of the stored record
     * @param args updated record for subsequent replacement in the storage
     * @return a (un)successful action message
     */
    String update(String[] args);

    /**
     * Deletes a record with provided Id if it was stored
     * @param id of the record to be deleted
     * @return a (un)successful action message
     */
    String delete(int id);
}
