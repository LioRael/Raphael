package com.faithl.raphael.controller.repo

import com.faithl.raphael.service.DocService
import com.faithl.raphael.vo.RapResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("/api/repo/doc")
class DocController {

    @Autowired
    lateinit var docService: DocService

    @RequestMapping("/list")
    fun list(repoId: String): RapResult {
        return docService.list(repoId)
    }

    @RequestMapping("/create")
    fun create(
        @RequestBody doc: Map<String, String?>,
    ): RapResult {
        return docService.create(doc["repoId"]!!, doc["parentId"], doc["type"]!!, doc["title"]!!, doc["content"])
    }

    @RequestMapping("/detail")
    fun detail(repoId: String, docId: String): RapResult {
        return docService.detail(repoId, docId)
    }

    @RequestMapping("/delete")
    fun delete(repoId: String, docId: String): RapResult {
        return docService.delete(repoId, docId)
    }

    @RequestMapping("/update")
    fun update(
        @RequestBody doc: Map<String, String?>,
    ): RapResult {
        return docService.update(doc["repoId"]!!, doc["docId"]!!, doc["title"]!!, doc["content"])
    }

    @RequestMapping("/move")
    fun move(
        repoId: String,
        docId: String,
        parentId: String?,
        index: Int,
    ): RapResult {
        return docService.move(repoId, docId, parentId, index)
    }

}