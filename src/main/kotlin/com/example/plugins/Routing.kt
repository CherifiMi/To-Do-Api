package com.example.plugins

import com.example.models.Task
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.request.*

val tasks = mutableListOf<Task>()

fun Application.configureRouting() {
    install(ContentNegotiation) {
        gson()
    }
    routing {
        apiCheck()

        getAllTasks()
        deleteAllTask()

        getTaskById()
        postTask()
        deleteTask()
    }
}

fun Routing.apiCheck() {
    get("/") {
        call.respondText("Hello Mito!")
    }
}

fun Routing.getAllTasks(){
    get("/tasks"){
        call.respond(tasks)
    }
}
fun Routing.deleteAllTask() {
    delete("/tasks"){
        tasks.removeAll(tasks)
        call.respond(tasks)
    }
}


fun Routing.getTaskById() {
    get("/task"){
        val id = call.request.queryParameters["id"]
        val task = tasks.find { it.id == id?.toInt() }
        val response = task ?: "User not found"
        call.respond(response)
    }
}
fun Routing.postTask() {
    post("/task"){
        val requestBody = call.receive<Task>()
        val task = requestBody.copy(id = tasks.size.plus(1))
        tasks.add(task)
        call.respond(task)
    }
}
fun Routing.deleteTask() {
    delete("/task"){
        val id = call.request.queryParameters["id"]
        tasks.removeIf {
            it.id == id?.toInt()
        }
        call.respondText("Task deleted")
    }
}
