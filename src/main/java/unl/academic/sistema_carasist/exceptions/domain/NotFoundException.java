package unl.academic.sistema_carasist.exceptions.domain;

public class NotFoundException extends RuntimeException{

    private static final String DESCRIPTION = "Not found Exception ";

    public NotFoundException(String detail){
        super(DESCRIPTION + ". " + detail);
    }
}
