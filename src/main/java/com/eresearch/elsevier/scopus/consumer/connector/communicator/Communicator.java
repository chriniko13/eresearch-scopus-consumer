package com.eresearch.elsevier.scopus.consumer.connector.communicator;

import java.net.URI;

public interface Communicator {

    String communicateWithElsevier(URI uri);
}
