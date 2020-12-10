package sbs.friendbet.repositories.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import sbs.friendbet.data.User;
import sbs.friendbet.repositories.UserRepo;

public class UserValidator implements Validator {

    private final UserRepo userRepo;

    public UserValidator(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "Name is blank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.empty", "Username is blank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.empty", "Password is blank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "confirmPassword.empty", "Confirm password is blank");

        User user = (User) o;

        if (user.getName().length() < 2 || user.getName().length() > 150) {
            errors.rejectValue("name", "name.error", "Invalid name");
        }
        if (userRepo.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "username.error", "Username taken!");
        }
        if (user.getPassword().length() < 6 || user.getConfirmPassword().length() < 6) {
            errors.rejectValue("password", "password.error", "password must at least be 6 symbols long");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "password.error", "The passwords didn't match");
        }

        try {
            double credit = Double.parseDouble(user.getCredit());
            if (credit < 0) {
                errors.rejectValue("credit", "credit.error", "Not enough credit");
            }
        } catch (NullPointerException ignore) {
            errors.rejectValue("name", "user.error", "Unable to creat new user");
        }
    }
}
