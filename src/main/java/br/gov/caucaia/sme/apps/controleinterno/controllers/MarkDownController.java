package br.gov.caucaia.sme.apps.controleinterno.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class MarkDownController {

    @Autowired
    private Resource markdownFile;

    @GetMapping("/edit")
    public String editMarkdown(Model model) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(markdownFile.getURL().getPath())));
            model.addAttribute("content", content);
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar o arquivo Markdown.");
        }
        return "editMarkdown";
    }

    @PostMapping("/save")
    public String saveMarkdown(@RequestParam("content") String content, Model model) {
        try {
            Files.write(Paths.get(markdownFile.getURL().getPath()), content.getBytes());
            model.addAttribute("message", "Arquivo salvo com sucesso!");
        } catch (IOException e) {
            model.addAttribute("error", "Erro ao salvar o arquivo Markdown.");
        }
        return "editMarkdown";
    }
}
