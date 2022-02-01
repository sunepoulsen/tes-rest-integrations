package dk.sunepoulsen.tes.rest.integrations.generators

import spock.lang.Specification

class UUIDRequestIdGeneratorSpec extends Specification {
    void "Next request id"() {
        when:
            String result = new UUIDRequestIdGenerator().generateId()

        then:
            int dashIndex = result.lastIndexOf('-')

            UUID.fromString(result.substring(0, dashIndex))
            Integer.valueOf(result.substring(dashIndex + 1)) == 1
    }
}
