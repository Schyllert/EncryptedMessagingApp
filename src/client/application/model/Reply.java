package client.application.model;

import java.util.Objects;

public class Reply {

    // Min value = -2147483648
    // Max value = 2147483647
    private Integer replyNumber;

    // A value that indicates which iteration
    private int iteration;

    private String message;

    public Reply(int replyNumber, String message) {
        this.replyNumber = replyNumber;
        this.message = message;
    }

    public int getReplyNumber() {
        return replyNumber;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reply reply = (Reply) o;
        return replyNumber == reply.replyNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(replyNumber);
    }
}
