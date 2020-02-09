package net.sharksystem.persons;

public interface OtherPerson extends Person {

    /**
     * The assurance level is a non-negative integer value between 10 and 0.
     * 10 means: identity of this person can be assured. An assurance level of 10 is only
     * possible if the owner actually met this person is reale life and exchanged keys. All other
     * identities are below 10.
     *
     * 0 means: identity can not be assured at all. This can be due to a lack of any certificate.
     * A zero can also be achieved if a certificate is present can only be verified over a queue of
     * issuers with a high certificate failure rate.
     *
     * The assurance level can be calculated for any other person with a certificate.
     *
     * Assume that the app owner has received a certificate for Alice signed by Bob. Also assumed
     * that the owner actually met Bob before. Assume also that the owner gave Bob a certificate
     * failure rate of 3. That means: The owner assumes Bob get fooled in 30% of certificate exchange.
     *
     * In other words: 70% of certificates signed by Bob are correct. The assurance level is 7.
     *
     * To make it more complex: Assume, the owner has not met Bob but got its certificate from
     * Clara which has got a certificate failure rate of 1 (lowest possible number) which means:
     * It is assumed that
     * Clara is fooled in 10% of certificate exchange. In other words: 90% of Claras' certificates are
     * correct. Bob has still a failure rate of 3. 70% of his certificates are correct.
     *
     * The owner calculates: 90 % * 70% = 0,9*0,7 = 0,63 = 63%. The assurance level is 6.
     *
     * Assurance level decreases with each additional verification step.
     *
     * @link getCertificateFailureRate()
     * @return assurance level
     */
    int getIdentityAssuranceLevel();

    /**
     * A positive integer value between 1 and 10 that describes how often a person accepts a wrong
     * certificate.
     *
     * Certificates are transmitted between persons. Humans make mistakes. We must deal with a failure
     * rate. Each participant in the Shark Network has to estimate how often others accept wrong
     * certificates. That's no blaming. We can have good friends who are - unfortunately - are
     * no very careful with certificates. They are still friends but their issued certificates
     * should be handled with care.
     *
     * The other way around. There could be people we actually do not like but who take great
     * care in certificate exchange. YOu don't have to spent your holidays with them but give
     * them credit.
     *
     * The best number is 1 which assumes that 10% of issued certificates are wrong. Number of 10 means:
     * 100% - any certificate is wrong. That is the worst number.
     *
     * Note: That value is for owners personal use only a will never be delivered to others through
     * the Shark Net.
     *
     * @return failure rate between 1 and 10.
     */
    int getCertificateFailureRate();
}
