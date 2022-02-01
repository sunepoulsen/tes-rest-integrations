package dk.sunepoulsen.tes.rest.integrations.generators;

import java.util.UUID;

/**
 * Constructs request ids based on an UUID and a sequence number.
 * <p>
 *     The sequence number is increased every time the request id
 *     is generated.
 * </p>
 * <p>
 *     The format of the request id is: <code>&lt;uuid&gt;-&lt;sequence number&gt;</code>
 * </p>
 */
public class UUIDRequestIdGenerator implements RequestIdGenerator {
    private UUID uuid;
    private int sequenceNo;

    public UUIDRequestIdGenerator() {
        this.uuid = UUID.randomUUID();
        this.sequenceNo = 0;
    }

    @Override
    public String generateId() {
        sequenceNo++;
        return uuid.toString() + "-" + sequenceNo;
    }
}
