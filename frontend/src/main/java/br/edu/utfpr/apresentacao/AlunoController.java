package br.edu.utfpr.apresentacao;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AlunoController {

    @GetMapping("/aluno")
    public String inicial(Model data) throws JsonSyntaxException, UnirestException {

        AlunoModel arrayAlunos[] = new Gson()
                    .fromJson(
                        Unirest
                            .get("http://localhost:8081/servico/aluno")
                            .asJson()
                            .getBody()
                            .toString(), 
                        AlunoModel[].class
                    );

        data.addAttribute("alunos", arrayAlunos);

        return "aluno-view";
    }

    @PostMapping ("/aluno/criar")
    public String criar(AlunoModel aluno) throws UnirestException {

            Unirest.post("http://localhost:8081/servico/aluno")
                .header("Content-type", "application/json")
                .header("accept", "application/json")
                .body(new Gson().toJson(aluno, AlunoModel.class))
                .asJson();

        return "redirect:/aluno";
    }

    @GetMapping ("/aluno/excluir")
    public String excluir (@RequestParam int id) throws UnirestException {

        Unirest
            .delete("http://localhost:8081/servico/aluno/{id}")
            .routeParam("id", String.valueOf(id))
            .asJson();

        return "redirect:/aluno";
    }

    @GetMapping ("/aluno/prepara-alterar")
    public String preparaAlterar (@RequestParam int id, Model data) throws JsonSyntaxException, UnirestException {

        AlunoModel alunoExistente = new Gson()
            .fromJson(
                Unirest
                    .get("http://localhost:8081/servico/aluno/{id}")
                    .routeParam("id", String.valueOf(id))
                    .asJson()
                    .getBody()
                    .toString(),
                AlunoModel.class
            );

        data.addAttribute("alunoAtual", alunoExistente);

        AlunoModel arrayAlunos[] = new Gson()
        .fromJson(
            Unirest
                .get("http://localhost:8081/servico/aluno")
                .asJson()
                .getBody()
                .toString(), 
            AlunoModel[].class
        );

        data.addAttribute("alunos", arrayAlunos);

        return "aluno-view-alterar";
    }

    @PostMapping ("/aluno/alterar")
    public String alterar (AlunoModel alunoAlterado) throws UnirestException {

        Unirest
            .put("http://localhost:8081/servico/aluno/{id}")
            .routeParam("id", String.valueOf(alunoAlterado.getId()))
            .header("Content-type", "application/json")
            .header("accept", "application/json")
            .body(new Gson().toJson(alunoAlterado, AlunoModel.class))
            .asJson();

        return "redirect:/aluno";
    }
}