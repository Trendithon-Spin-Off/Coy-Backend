package Trendithon.SpinOff.domain.profile.valid.exception;

import java.util.NoSuchElementException;

public class CheckMemberIdNotNullException extends NoSuchElementException {
    public CheckMemberIdNotNullException(String message) {
        super(message);
    }
}
