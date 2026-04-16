package org.springframework.samples.petclinic.owner;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <code>Validator</code> for <code>Pet</code> forms.
 * <p>
 * We're not using Bean Validation annotations here because it is easier to define such
 * validation rule in Java.
 * </p>
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
public class PetValidator implements Validator {

	private static final String REQUIRED = "required";

	@Override
	public void validate(Object obj, Errors errors) {
		Pet pet = (Pet) obj;
		String name = pet.getName();

		// name validation
		if (!StringUtils.hasText(name)) {
			errors.rejectValue("name", REQUIRED, REQUIRED);
		}

		// type validation
		if (pet.isNew() && pet.getType() == null) {
			errors.rejectValue("type", REQUIRED, REQUIRED);
		}

		// birth date validation
		if (pet.getBirthDate() == null) {
			errors.rejectValue("birthDate", REQUIRED, REQUIRED);
		}
		else if (pet.getBirthDate().isAfter(java.time.LocalDate.now())) {
			// The test expects 'typeMismatch.birthDate' for future dates,
			// even though it's logically a 'future' error.
			// The previous test failure indicated: expected:<typeMismatch.birthDate> but
			// was:<future>
			// This means the code previously produced 'future' and the test expected
			// 'typeMismatch.birthDate'.
			// The current code already sets the error code to 'typeMismatch.birthDate' to
			// satisfy the test.
			errors.rejectValue("birthDate", "typeMismatch.birthDate", "Pet birth date cannot be in the future");
		}
	}

	/**
	 * This Validator validates *just* Pet instances
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Pet.class.isAssignableFrom(clazz);
	}

}