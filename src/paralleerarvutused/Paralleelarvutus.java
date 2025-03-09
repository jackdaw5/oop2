package paralleerarvutused;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Paralleelarvutus {

    static class FileResult {
        String fileName;
        BigInteger maxNumber;
        BigInteger sum;

        public FileResult(String fileName, BigInteger maxNumber, BigInteger sum) {
            this.fileName = fileName;
            this.maxNumber = maxNumber;
            this.sum = sum;
        }
    }


    static class Worker implements Runnable {
        private BlockingQueue<String> inputQueue;
        private BlockingQueue<FileResult> outputQueue;

        public Worker(BlockingQueue<String> inputQueue, BlockingQueue<FileResult> outputQueue) {
            this.inputQueue = inputQueue;
            this.outputQueue = outputQueue;
        }

        @Override
        public void run() {
            try {
                String fileName = inputQueue.take();
                processFile(fileName);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void processFile(String fileName) {
            try {
                BufferedReader reader = Files.newBufferedReader(Paths.get(fileName));
                String line;
                BigInteger sum = BigInteger.ZERO;
                BigInteger max = BigInteger.ZERO;

                while ((line = reader.readLine()) != null) {
                    String[] numbers = line.split("\\s+");
                    for (String number : numbers) {
                        BigInteger currentNumber = new BigInteger(number);
                        sum = sum.add(currentNumber);
                        if (currentNumber.compareTo(max) > 0) {
                            max = currentNumber;
                        }
                    }
                }

                reader.close();

                outputQueue.put(new FileResult(fileName, max, sum));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            System.out.println("Palun edastage sisendfailide nimed käsurea parameetritena.");
            return;
        }

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
        BlockingQueue<FileResult> outputQueue = new LinkedBlockingQueue<>();


        for (String arg : args) {
            inputQueue.put(arg);
        }


        List<Thread> workers = new ArrayList<>();
        for (int i = 0; i < availableProcessors; i++) {
            Thread workerThread = new Thread(new Worker(inputQueue, outputQueue));
            workerThread.start();
            workers.add(workerThread);
        }


        BigInteger totalSum = BigInteger.ZERO;
        BigInteger maxNumber = BigInteger.ZERO;
        String maxFile = "";
        BigInteger minSum = null;
        String minFile = "";

        int processedFiles = 0;
        while (processedFiles < args.length) {
            FileResult result = outputQueue.take();
            totalSum = totalSum.add(result.sum);
            if (result.maxNumber.compareTo(maxNumber) > 0) {
                maxNumber = result.maxNumber;
                maxFile = result.fileName;
            }
            if (minSum == null || result.sum.compareTo(minSum) < 0) {
                minSum = result.sum;
                minFile = result.fileName;
            }
            processedFiles++;
        }


        for (Thread worker : workers) {
            worker.join();
        }


        System.out.println("Kõikide arvude kogusumma: " + totalSum);
        System.out.println("Kõige suurem arv: " + maxNumber + ", fail: " + maxFile);
        System.out.println("Kõige väiksema summa omanik fail: " + minFile);
    }
}

