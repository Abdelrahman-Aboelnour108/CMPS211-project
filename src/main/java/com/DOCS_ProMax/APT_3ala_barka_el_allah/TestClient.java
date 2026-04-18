package com.DOCS_ProMax.APT_3ala_barka_el_allah;

/**
 * Manual smoke-test client.
 *
 * HOW TO RUN:
 *   1. Start the Spring Boot server (run Apt3alaBarkaElAllahApplication.main).
 *      Watch for "Client connected" and "Session created" in the server console.
 *   2. In a second terminal / run configuration, execute TestClient.main.
 *
 * Expected console output (server side):
 *   [Server] Client connected: <id>
 *   [Server] Session created: XXXXXX by Alice
 *
 * Expected console output (client side):
 *   [Client] Connected to server (HTTP status 101)
 *   [Client] Session created: XXXXXX
 *   [TestClient] ✓ All checks passed.
 */
public class TestClient {

    public static void main(String[] args) throws Exception {

        Clock      sharedClock = new Clock();
        BlockCRDT  localDoc    = new BlockCRDT(1, sharedClock);
        BlockNode  block       = localDoc.insertTopLevelBlock(new CharCRDT(1, sharedClock));

        // Connect to the running Spring Boot server
        Client client = new Client("ws://localhost:8080/collab", localDoc, sharedClock, block.getId());

        // Track what the server sends back
        final String[] receivedType = {null};
        final String[] receivedCode = {null};

        client.setMessageListener(op -> {
            receivedType[0] = op.type;
            if (op.sessionCode != null) receivedCode[0] = op.sessionCode;
        });

        client.connectBlocking();   // blocks until the WebSocket handshake completes
        System.out.println("[TestClient] WebSocket open: " + client.isOpen());

        // Ask the server to create a session
        client.createSession("Alice");

        // Give the server up to 3 seconds to respond
        long deadline = System.currentTimeMillis() + 3_000;
        while (receivedType[0] == null && System.currentTimeMillis() < deadline) {
            Thread.sleep(50);
        }

        // ---- Assertions ----
        boolean connected = client.isOpen();
        boolean gotCreated = "SESSION_CREATED".equals(receivedType[0]);
        boolean hasCode = receivedCode[0] != null && receivedCode[0].length() == 6;

        System.out.println("[TestClient] isOpen           : " + connected);
        System.out.println("[TestClient] SESSION_CREATED  : " + gotCreated);
        System.out.println("[TestClient] session code (6) : " + receivedCode[0]);

        if (connected && gotCreated && hasCode) {
            System.out.println("[TestClient] ✓ All checks passed.");
        } else {
            System.err.println("[TestClient] ✗ Some checks FAILED — see above.");
        }

        client.closeBlocking();
    }
}