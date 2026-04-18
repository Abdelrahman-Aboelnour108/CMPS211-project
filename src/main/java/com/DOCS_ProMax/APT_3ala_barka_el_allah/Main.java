public static void main(String[] args) {
    // Initialize the document for User 1
    Document doc = new Document(1);

    System.out.println("--- Starting Test ---");

    // Insert characters to make "HELLO"
    doc.LocalInsert('H', 0);
    doc.LocalInsert('E', 1);
    doc.LocalInsert('L', 2);
    doc.LocalInsert('L', 3);
    doc.LocalInsert('O', 4);
    System.out.println("Initial string: " + doc.RenderDocument()); // Expected: HELLO

    // Delete from the middle (The second 'L')
    // Index 0:H, 1:E, 2:L, 3:L, 4:O
    doc.LocalDelete(3);
    System.out.println("After deleting index 3: " + doc.RenderDocument()); // Expected: HELO

    //  Delete first character (Index 0)
    doc.LocalDelete(0);
    System.out.println("After deleting index 0: " + doc.RenderDocument()); // Expected: ELO

    //  Insert at the NEW index 0 (putting a 'B' before 'E')
    doc.LocalInsert('B', 0);
    System.out.println("After inserting B at index 0: " + doc.RenderDocument()); // Expected: BELO

    System.out.println("--- Test Complete ---");
}