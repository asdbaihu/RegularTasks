package hello.Controller;


import hello.Model.BashRepository;
import hello.Model.Bashscript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.sql.Timestamp;


@Component
public class BashScriptController {
/*    public BashScriptController(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                findAll();
            }
        }).start();

    }*/

    @Autowired
    private BashRepository bashRepository;

    volatile Iterable<Bashscript> bashscripts;

    private void findAll(){
        while (true) {
            try {
                bashscripts = bashRepository.findAll();

                bashscripts.forEach(bashscript -> {if (bashscript.getNextexecution().before
                        (new Timestamp(System.currentTimeMillis()))){
                    if (bashscript.getFrequency() != null){
                        saveNewNextExecution(bashscript);
                        ThreadPoolTask.threadPool.submit(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println(Thread.currentThread().getName() + " task id " + bashscript.getId());
                                executeCmdCommand(bashscript, true);
                            }
                        });
                    } else {
                        ThreadPoolTask.threadPool.submit(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println(Thread.currentThread().getName() + " task id " + bashscript.getId());
                                executeCmdCommand(bashscript, false);
                            }
                        });
                    }
                }});
            } catch (NullPointerException e){
                System.out.println("no elements");
            }

        }
    }

    private void saveNewNextExecution(Bashscript bashscript){
        Bashscript newBashScrip = new Bashscript();
        newBashScrip = bashscript;
        newBashScrip.setNextexecution(new Timestamp(System.currentTimeMillis()+ shouldConvertTimeBecauseJavaCannotByDepricatedWay(bashscript.getFrequency())));
        bashRepository.delete(bashscript.getId());
        bashRepository.save(newBashScrip);
    }

    private long shouldConvertTimeBecauseJavaCannotByDepricatedWay(Time time){
        long timeValue;
        timeValue = time.getHours()* 3600000 + time.getMinutes() * 60000 + time.getSeconds() * 1000;
        return timeValue;
    }



    private void executeCmdCommand(Bashscript bashscript, boolean multipleTimes){
        String command = bashscript.getScript();
        String commandForCmd = "";
        String[] commands = command.split(";");
        if (commands.length > 1){
            for (int i = 0; i < commands.length; i++) {
                if (i == commands.length - 1){
                    commandForCmd += commands[i];
                } else {
                    commandForCmd += commands[i] + " && ";
                }
            }
        } else {
            commandForCmd = command;
        }
        System.out.println(commandForCmd);
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", commandForCmd);
        builder.redirectErrorStream();
        try {
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            /*while ((line=reader.readLine()) != null){
                System.out.println(line);
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!multipleTimes){
            bashRepository.delete(bashscript.getId());

        }
    }




}
