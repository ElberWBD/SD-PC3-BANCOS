package Distribuido.Proyecto.Controller;

import Distribuido.Proyecto.Bancos.BancoNode;
import Distribuido.Proyecto.Distributed.RequestMessage;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/distributed")
public class DistributedController {

    private final BancoNode local;

    public DistributedController(BancoNode local) { this.local = local; }

    @PostMapping("/request")
    public void receiveRequest(@RequestBody RequestMessage msg) {
        local.handleRequest(msg);
    }

    @PostMapping("/ok")
    public void receiveOk(@RequestBody RequestMessage msg) {
        local.handleOk(msg);
    }

    @PostMapping("/release")
    public void receiveRelease(@RequestBody RequestMessage msg) {
        local.handleRelease(msg);
    }
}
