package softuni.exam.util.impl;

import jdk.jshell.spi.ExecutionControl;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import softuni.exam.util.ValidatorUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidatorUtilImpl implements ValidatorUtil {

    //@Autowired
    private final Validator validator;

    @Autowired
    public ValidatorUtilImpl() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public <E> boolean isValid(E entity) {
        //throw  new NotYetImplementedException("Not implemented");
        return validator.validate(entity).isEmpty();
    }

    @Override
    public <E> Set<ConstraintViolation<E>> violations(E entity) {
        //throw  new NotYetImplementedException("Not implemented");
        return validator.validate(entity);
    }

}
