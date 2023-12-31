import java.io.*;
import java.util.PriorityQueue;

/**
 * Class that encodes and decodes text using Huffman coding.
 */
public class Huffman {
    /**
     * Class that represents nodes in a Huffman tree.
     */
    public static class Node implements Comparable<Node> {
        char character = '\0';
        int frequency;
        Node left = null;
        Node right = null;
        public int compareTo(Node other) {
            return this.frequency - other.frequency;
        }

        /**
         * Constructor for leaf nodes.
         */
        public Node(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }

        /**
         * Constructor for inner nodes.
         */
        public Node(int frequency, Node left, Node right) {
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }
    }
    /**
     * Method for compressing a file using Huffman encoding.
     */
    public static void compress(String file, String compressedFile) {
        int[] frequencies = calculateFrequencies(file);
        Node root = buildHuffmanTree(frequencies);
        String[] huffmanCodes = getHuffmanCodes(root);
        writeCompressedFile(file, compressedFile, frequencies, huffmanCodes);
        System.out.println("File successfully compressed");
    }

    /**
     * Method that calculates frequencies
     * for the different characters in a file.
     */
    private static int[] calculateFrequencies(String file) {
        int[] frequencies = new int[256];
        try {
            // Reading from the file
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            int character;
            // Returns -1 when no more characters left to read
            while ((character = bufferedInputStream.read()) != -1) {
                frequencies[character]++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return frequencies;
    }

    /**
     * Method that creates a HuffmanTree based on
     * the frequencies for the different characters.
     */
    public static Node buildHuffmanTree(int[] frequencies) {
        PriorityQueue<Node> priorityQueue = getLeafNodes(frequencies);
        while (priorityQueue.size() > 1) {
            Node leftChild = priorityQueue.poll();
            Node rightChild = priorityQueue.poll();
            assert rightChild != null;
            int parentFrequency = leftChild.frequency + rightChild.frequency;
            Node parent = new Node(parentFrequency, leftChild, rightChild);
            priorityQueue.offer(parent);
        }
        return priorityQueue.poll();
    }
    /**
     * Create a PriorityQueue that stores the LeafNodes of the HuffmanTree.
     */
    public static PriorityQueue<Node> getLeafNodes(int[] frequencies) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        // Loop through all frequencies, if value is not zero then make a Node for it
        for (int i = 0; i < 256; i++) {
            if (frequencies[i] > 0) {
                char character = (char) i;
                int frequency = frequencies[i];
                Node leafNode = new Node(character, frequency);
                priorityQueue.offer(leafNode);
            }
        }
        return priorityQueue;
    }

    /**
     * Method for getting the HuffmanCodes for the different characters.
     */
    private static String[] getHuffmanCodes(Node root) {
        String[] huffmanCodes = new String[256];
        generateHuffmanCodes(root, "", huffmanCodes);
        return huffmanCodes;
    }

    /**
     * Method that generates the HuffmanCodes for the different characters.
     */
    public static void generateHuffmanCodes(Node root, String code, String[] huffmanCodes) {
        if (root == null) {
            return;
        }
        // Checking if it is a leaf node.
        if (isLeaf(root)) {
            huffmanCodes[root.character] = code;
        }
        generateHuffmanCodes(root.left, code + "0", huffmanCodes);
        generateHuffmanCodes(root.right, code + "1", huffmanCodes);
    }

    /**
     * Method that writes the frequencies and Huffman codes to the new file.
     */
    private static void writeCompressedFile(String uncompressedFile, String compressedFile, int[] frequencies, String[] huffmanCodes) {
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(uncompressedFile));
             DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(compressedFile))) {

            // Write frequencies to the beginning of the compressed file
            // They are written as four bytes (using the .writeInt method)
            // In total the frequencies take up (4 * 256) bytes = 1 kilobyte space in the file.
            for (int frequency : frequencies) {
                outputStream.writeInt(frequency);
            }
            // Write Huffman codes to the compressed file
            int bitBuffer = 0;
            int bitCount = 0;
            int character;

            // Program will loop until character is -1 (which indicates end of file)
            while ((character = inputStream.read()) != -1) {
                String huffmanCode = huffmanCodes[character];
                // Process the Huffman code bit by bit
                for (char bit : huffmanCode.toCharArray()) {
                    // Shifts the current content of bitBuffer one position to the left, making space for the next bit (the one we are adding).
                    // Then the next bit will be added after this, this way we get the bits (0s and 1s) stored in the order they are added.
                    // The first bit that is stored the farthest left, and the last is stored farthest right.
                    // For example the huffman code 11010100 will be stored as 0b11010100 = 212.
                    // We also convert the character '0' or '1' to their integer value (0 or 1) (since we read the bits as char).
                    bitBuffer = (bitBuffer << 1) | (bit - '0');
                    bitCount++;

                    // When 8 bits are added to bitBuffer, write them as a byte to outputStream
                    if (bitCount == 8) {
                        outputStream.write(bitBuffer);
                        bitBuffer = 0;
                        bitCount = 0;
                    }
                }
            }
            // Write any remaining bits to the output file
            if (bitCount > 0) {
                // Shift the bitBuffer left the amount of bits remaining (to make space for all of them), and then write them to outputStream
                // We end up with a byte that is not full, but no way to avoid this as far as I know.
                outputStream.write(bitBuffer << (8 - bitCount));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Method that decompresses a HuffmanCompressed file to its original state.
     */
    public static void decompress(String compressedFile, String decompressedFile) {
        // Reading from the compressedFile
        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(compressedFile));
             // Writing to the decompressedFile
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(decompressedFile))) {
                // Read character frequencies from the compressed file
                // They are stored at the beginning of the compressed file
                int[] frequencies = readFrequencies(inputStream);

                // Build a Huffman tree based on the frequencies
                Node root = buildHuffmanTree(frequencies);

                // Calculate the total amount of characters in the original file
                int amountCharacters = getAmountOfCharacters(frequencies);

                // Decode the Huffman codes in the compressed file and write it to the decompressed file
                decodeAndWriteData(inputStream, outputStream, root, amountCharacters);
            System.out.println("File successfully decompressed");
        } catch (IOException e) {
            e.printStackTrace();
        }
     }

    /**
     * Method for getting the frequencies for the different characters from the compressed file.
     * They are stored at the beginning of the compressed file, and we know there are 256 total entries for frequencies
     * (which is why we read until we reach 256 since after that is when the Huffman codes start to be stored)
     * In short, in the compressed file we have 256 entries for frequencies at the start of the file.
     */
    private static int[] readFrequencies(DataInputStream inputStream) throws IOException {
        int[] frequencies = new int[256];
        for (int i = 0; i < 256; i++) {
            // Since the frequencies were written as four bytes
            // .readInt() reads the four next bytes and interpret it as an Int
            // This works well since .writeInt() wrote the
            // frequencies as four bytes when storing them
            frequencies[i] = inputStream.readInt();
        }
        return frequencies;
    }

    /**
     * Method for decoding the Huffman codes in the compressed file.
     */
    private static void decodeAndWriteData(DataInputStream inputStream, BufferedOutputStream outputStream, Node root, int amountCharacters) throws IOException {
        Node current = root;
        int data;
        // Returns -1 when no more characters left to read
        while ((data = inputStream.read()) != -1) {
            for (int i = 7; i >= 0; i--) {
                // Get a single bit from the data byte by shifting the bits of data to the right by i positions
                // First its by 7 positions, then by 6 and then 5. This way we are able to process all the bits in correct order.
                // This is because the bit in the first position is stored the farthest left in the byte (7 positions to the left).
                // Since we shift the byte by i positions we then we get this first bit, then the second and so on.
                // For example since the huffman code 11010100 was stored as 0b11010100.
                // By doing what was described above first we get the bit 1, then 1, then 0 and so on.
                // Then we use a bitwise AND operation with 1 so that we only get this bit that is shifted.
                // This entire operation isolates the i-th bit so that we can process it
                int bit = (data >> i) & 1;

                // Traverse the Huffman tree based on the bit value (0 or 1).
                if (bit == 0) {
                    current = current.left;
                } else {
                    current = current.right;
                }

                assert current != null;
                // When a leaf node is reached, write the character to the output stream.
                if (isLeaf(current)) {
                    outputStream.write(current.character);
                    // Reset to the root of the Huffman tree for the next character.
                    current = root;
                    amountCharacters--;
                }
                // Since the last byte in the file might not be full, we need to account for this
                // We calculate the total amount of characters in the original file from the frequencies
                // Whenever we add a character to the deCompressed file we decrement this amount
                if(amountCharacters == 0) {
                    break;
                }
            }
        }
    }

    /**
     * Check if a Node is a leaf node
     */
    public static boolean isLeaf(Node node) {
        return node.left == null && node.right == null;
    }

    /**
     * Method for making a file name for the new compressed file
     */
    public static String compressFilename(String originalFileName) {
        File originalFile = new File(originalFileName);
        String fileName = originalFile.getName();
        String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);

        return nameWithoutExtension + "Compressed." + fileExtension;
    }

    /**
     * Method for making a file name for the new decompressed file
     */
    public static String decompressFilename(String originalFileName) {
        File originalFile = new File(originalFileName);
        String fileName = originalFile.getName();
        String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);

        if (nameWithoutExtension.endsWith("Compressed")) {
            nameWithoutExtension = nameWithoutExtension.substring(0, nameWithoutExtension.lastIndexOf("Compressed"));
        }
        return nameWithoutExtension + "Decompressed." + fileExtension;
    }

    /**
     * Method for calculating the total amount of characters in the original file
     */
    public static int getAmountOfCharacters(int[] frequencies) {
        int amountSymbols = 0;
        for(int frequency : frequencies) {
            amountSymbols += frequency;
        }
        return amountSymbols;
    }

    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Usage: java Huffman <file> <compress/decompress)>");
            return;
        }
        String file = args[0];
        String operation = args[1];

        if (operation.equalsIgnoreCase("compress")) {
            String outputFile = compressFilename(file);
            compress(file, outputFile);
        } else if (operation.equalsIgnoreCase("decompress")) {
            String outputFile = decompressFilename(file);
            decompress(file, outputFile);
        } else {
            System.out.println("Invalid operation. Use 'compress' or 'decompress'.");
        }
    }
}
