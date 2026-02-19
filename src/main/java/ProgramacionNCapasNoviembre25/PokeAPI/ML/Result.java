
package ProgramacionNCapasNoviembre25.PokeAPI.ML;

import java.util.List;

public class Result {
    public boolean Correct;
    public String ErrorMessage;
    public Object Object;
    public List<Object> Objects;
    public Exception ex;

    public boolean isCorrect() {
        return Correct;
    }

    public void setCorrect(boolean Correct) {
        this.Correct = Correct;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String ErrorMessage) {
        this.ErrorMessage = ErrorMessage;
    }

    public Object getObject() {
        return Object;
    }

    public void setObject(Object Object) {
        this.Object = Object;
    }

    public List<Object> getObjects() {
        return Objects;
    }

    public void setObjects(List<Object> Objects) {
        this.Objects = Objects;
    }

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }
    
    
}
