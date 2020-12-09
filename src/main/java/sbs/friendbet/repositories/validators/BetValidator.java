package sbs.friendbet.repositories.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import sbs.friendbet.data.Bet;

public class BetValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Bet.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "name must be longer than two characters long");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "oddsName", "oddsName.empty", "oddsName must be longer than two characters long");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "odds", "odds.empty", "odds cant be empty");

        Bet bet = (Bet) o;
        if (bet.getName().length() < 2 || bet.getName().length() > 150){
            errors.rejectValue("name", "Name.Length", "Name length not valid");
        }
        if (bet.getOddsName().length() < 2 || bet.getOddsName().length() > 150){
            errors.rejectValue("oddsName", "oddsName.Length", "OddsName length not valid");
        }

        if (bet.getDescription().length() > 5000) {
            errors.rejectValue("description", "Description.Length", "Description to long.");
        }
    }
}
