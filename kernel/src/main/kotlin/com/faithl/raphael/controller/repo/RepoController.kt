package com.faithl.raphael.controller.repo

import com.faithl.raphael.service.RepoService
import com.faithl.raphael.vo.RapResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("/api/repo")
class RepoController {

    @Autowired
    lateinit var repoService: RepoService

    @RequestMapping("/all")
    fun query(): RapResult {
        return repoService.query()
    }

    @RequestMapping("/list")
    fun list(workspace: String): RapResult {
        return repoService.list(workspace)
    }

    @RequestMapping("/create")
    fun create(
        workspace: String,
        name: String,
        description: String,
        visible: String,
    ): RapResult {
        return repoService.create(name, description, visible, workspace)
    }

    @RequestMapping("/delete")
    fun delete(id: String): RapResult {
        return repoService.delete(id)
    }

    @RequestMapping("/detail")
    fun detail(repoName: String, workspace: String): RapResult {
        return repoService.detail(repoName, workspace)
    }

}