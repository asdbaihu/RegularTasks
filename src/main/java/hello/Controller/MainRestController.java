package hello.Controller;

import hello.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping(path = "/infocom")
public class MainRestController {

    @Autowired
    private BashRepository bashRepository;
    @Autowired
    private GenerateExelRepository generateExelRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private ForSendingEmail forSendingEmailRepository;


    // save new Email settings
    @RequestMapping(value = "/emailsettings/", method = RequestMethod.POST)
    public ResponseEntity<ForSendingEmail> saveEmailSettings(@RequestBody ForSendingEmail forSendingEmail) {

        forSendingEmailRepository.setEmail(forSendingEmail.getEmail());
        forSendingEmailRepository.setPassword(forSendingEmail.getPassword());
        forSendingEmailRepository.setText(forSendingEmail.getText());

        return new ResponseEntity<ForSendingEmail>(forSendingEmail, HttpStatus.OK);
    }

    // load all email settings
    @RequestMapping(value = "/emailsettings/", method = RequestMethod.GET)
    public ResponseEntity<ForSendingEmail> showEmailSettings() {
        ForSendingEmail forSendingEmail = new ForSendingEmail();

        forSendingEmail.setEmail(forSendingEmailRepository.getEmail());
        forSendingEmail.setPassword(forSendingEmailRepository.getPassword());
        forSendingEmail.setText(forSendingEmailRepository.getText());

        return new ResponseEntity<ForSendingEmail>(forSendingEmail, HttpStatus.OK);
    }



    // load all bashes
    @RequestMapping(value = "/bashes",
            params = {"page", "size"},
            method = RequestMethod.GET)
    public Page<Bashscript> showAllBash(
            @RequestParam("page") int page, @RequestParam("size") int size
    ) {
        return bashRepository.findAll(new PageRequest(page,size));
    }

// find exact bash
    @RequestMapping(value = "/bashes/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Bashscript getBash(@PathVariable("id") Long id) {
        return bashRepository.findOne(id);
    }
// save new bash
    @RequestMapping(value = "/bashes/", method = RequestMethod.POST)
    public Bashscript save(@RequestBody Bashscript bashscript) {
        return bashRepository.save(bashscript);
    }
// delete bash
    @RequestMapping(value = "/bashes/{id}", method = RequestMethod.DELETE)
    public Bashscript delete(@PathVariable("id") Long id) {


        Bashscript bashscript = bashRepository.findOne(id);
        if (bashscript == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        bashRepository.delete(id);
        return bashscript;
    }
// update bash
    @RequestMapping(value = "/bashes/{id}", method = RequestMethod.PUT)
    public Bashscript updateBash(@PathVariable("id") Long id, @RequestBody Bashscript bashscript) {

        Bashscript bashscript1 = bashRepository.findOne(id);

        if (!bashRepository.exists(id)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        bashscript1.setId(id);
        bashscript1.setScript(bashscript.getScript());
        bashscript1.setTimedate(bashscript.getTimedate());
        bashscript1.setFrequency(bashscript.getFrequency());
        bashscript1.setNextexecution(bashscript.getNextexecution());



        bashRepository.delete(id);
        bashRepository.save(bashscript1);
        return bashscript1;
    }

///////////////
    // Exel task
    // load all exel file wich should be generated
    @RequestMapping(value = "/exel",
            params = {"page", "size"},
            method = RequestMethod.GET)
    public Page<Generateexel> showAllExelFiles(
            @RequestParam("page") int page, @RequestParam("size") int size
    ) {
        return generateExelRepository.findAll(new PageRequest(page,size));
    }

    // save new exel file
    @RequestMapping(value = "/exel/", method = RequestMethod.POST)
    public Generateexel save(@RequestBody Generateexel generateexel) {
        return generateExelRepository.save(generateexel);
    }

    // find exact exel
    @RequestMapping(value = "/exel/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Generateexel getExel(@PathVariable("id") Long id) {
        return generateExelRepository.findOne(id);
    }

    // delete bash
    @RequestMapping(value = "/exel/{id}", method = RequestMethod.DELETE)
    public Generateexel deleteExel(@PathVariable("id") Long id) {


        Generateexel generateexel = generateExelRepository.findOne(id);
        if (generateexel == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        generateExelRepository.delete(id);
        return generateexel;
    }
    // update bash
    @RequestMapping(value = "/exel/{id}", method = RequestMethod.PUT)
    public Generateexel updateBash(@PathVariable("id") Long id, @RequestBody Generateexel generateexel) {

        Generateexel generateexelNew = generateExelRepository.findOne(id);

        if (!generateExelRepository.exists(id)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        generateexelNew.setId(id);
        generateexelNew.setScript(generateexel.getScript());
        generateexelNew.setTimedate(generateexel.getTimedate());
        generateexelNew.setFrequency(generateexel.getFrequency());

        generateExelRepository.delete(id);
        generateExelRepository.save(generateexelNew);
        return generateexelNew;
    }

    /////////////////////
    @RequestMapping(path="/tasks")
    public ModelAndView showTasks() {

        return new ModelAndView("tasks");
    }

    ///////////////////////
    ///////////////
    // Email task
    // load list of emails wich should be generated
   @RequestMapping(value = "/email",
            params = {"page", "size"},
            method = RequestMethod.GET)
    public Page<Sendemail> showAllEmails(
            @RequestParam("page") int page, @RequestParam("size") int size
    ) {
        return emailRepository.findAll(new PageRequest(page,size));
    }


    // save new exel file
    @RequestMapping(value = "/email/", method = RequestMethod.POST)
    public Sendemail save(@RequestBody Sendemail sendEmail) {
        return emailRepository.save(sendEmail);
    }

    // find exact exel
    @RequestMapping(value = "/email/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Sendemail getEmail(@PathVariable("id") Long id) {
        return emailRepository.findOne(id);
    }

    // delete bash
    @RequestMapping(value = "/email/{id}", method = RequestMethod.DELETE)
    public Sendemail deleteEmail(@PathVariable("id") Long id) {


        Sendemail sendEmail = emailRepository.findOne(id);
        if (sendEmail == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        emailRepository.delete(id);
        return sendEmail;
    }
    // update bash
    @RequestMapping(value = "/email/{id}", method = RequestMethod.PUT)
    public Sendemail updateEmail(@PathVariable("id") Long id, @RequestBody Sendemail sendEmail) {

        Sendemail sendEmailNew = emailRepository.findOne(id);

        if (!emailRepository.exists(id)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        sendEmailNew.setId(id);
        sendEmailNew.setScript(sendEmail.getScript());
        sendEmailNew.setTimedate(sendEmail.getTimedate());
        sendEmailNew.setFrequency(sendEmail.getFrequency());

        emailRepository.delete(id);
        emailRepository.save(sendEmailNew);
        return sendEmailNew;
    }


}
