package pt.ulisboa.tecnico.softeng.bank.controller;

import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;


@Controller
@RequestMapping(value = "/banks/bank/{code}/clients")
public class ClientController {
    private static Logger logger = LoggerFactory.getLogger(ClientController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String clientForm(Model model, @PathVariable String code){
        logger.info("ClientForm");
        Bank bank = Bank.getBankByCode(code);
        logger.info("Bank to use: {}", bank);
        model.addAttribute("bank", bank);
        model.addAttribute("client", new Client());
        model.addAttribute("clients", Bank.getBankByCode(code).getClients());

        return "bank";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String clientSubmit(Model model,@PathVariable String code, @ModelAttribute Client client){
        logger.info("clientSubmit info: name: {} id: {} age: {} bank: {}" , client.getName(),client.getId(),client.getAge(), Bank.getBankByCode(code));

        try{
            new Client(Bank.getBankByCode(code), client.getId(), client.getName(), client.getAge());

        }catch(BankException be){
            model.addAttribute("error", "Error: it was not possible to create the client");
            model.addAttribute("client", new Client());
            model.addAttribute("clients", Bank.getBankByCode(code).getClients());
            return "/banks/bank/"+client.getBank().getCode()+"/clients";
        }

        return "redirect:/banks/bank/"+code;
    }

    @RequestMapping(value="/client/{id}", method=RequestMethod.GET)
    public String clientShow(Model model, @PathVariable String code, @PathVariable String id){
        logger.info("clientShow bankCode: {} clientId: {}", code,id);
        for(Client client : Bank.getBankByCode(code).getClients()){
            if(client.getId().equals(id)){
                model.addAttribute("client", client);
                return "client";
            }
        }
        model.addAttribute("error", "Error: client with id " + id + " doesn't exist.");
        return "bank";
    }

}
