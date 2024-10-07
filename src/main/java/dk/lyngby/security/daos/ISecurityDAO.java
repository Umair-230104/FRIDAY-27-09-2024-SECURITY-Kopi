package dk.lyngby.security.daos;


import dk.bugelhartmann.UserDTO;
import dk.lyngby.security.entities.User;
import dk.lyngby.security.exceptions.ValidationException;

public interface ISecurityDAO {
    UserDTO getVerifiedUser(String username, String password) throws ValidationException;
    User createUser(String username, String password);
}
