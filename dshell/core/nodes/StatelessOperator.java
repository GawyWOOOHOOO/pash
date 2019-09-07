package dshell.core.nodes;

import dshell.core.DFileSystem;
import dshell.core.Operator;
import dshell.core.worker.RemoteExecutionData;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.rmi.Remote;

public class StatelessOperator extends Operator<Object, Object> {
    private final int parallelizationHint;

    public StatelessOperator(String program) {
        super(program);
        this.parallelizationHint = 1;
    }

    public StatelessOperator(String program, int parallelizationHint) {
        super(program);
        this.parallelizationHint = parallelizationHint;
    }

    public StatelessOperator(String program, String[] commandLineArguments) {
        super(program, commandLineArguments);
        this.parallelizationHint = 1;
    }

    public StatelessOperator(String program, String[] commandLineArguments, int parallelizationHint) {
        super(program, commandLineArguments);
        this.parallelizationHint = parallelizationHint;
    }

    @Override
    public void next(Object data) {
        try {
            // creating new process
            ProcessBuilder processBuilder = new ProcessBuilder(program, getArgumentsAsString());
            Process process = processBuilder.start();

            if (!process.isAlive() && process.exitValue() != 0)
                throw new RuntimeException("Execution of program '" + program + "' returned with exit value " + process.exitValue());

            // getting standard input
            if (data != null) {
                OutputStream os = process.getOutputStream();
                RemoteExecutionData d = (RemoteExecutionData)data;

                // write to standard input of the process
                os.write(d.getInputData());
                os.flush();
                os.close();
            }

            byte[] systemOut = process.getInputStream().readAllBytes();

            // waiting for process to finish and terminating it
            process.waitFor();
            process.destroy();

            RemoteExecutionData red = new RemoteExecutionData(nextOperator, systemOut, "localhost", 4000);
            consumer.next(red);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getParallelizationHint() {
        return parallelizationHint;
    }
}