package org.example

/**
 * За основу берём код решения домашнего задания из предыдущего семинара и дорабатываем его.
 *
 * — Создайте иерархию sealed классов, которые представляют собой команды. В корне иерархии интерфейс Command.
 * — В каждом классе иерархии должна быть функция isValid(): Boolean, которая возвращает true, если команда
 * введена с корректными аргументами. Проверку телефона и email нужно перенести в эту функцию.
 * — Напишите функцию readCommand(): Command, которая читает команду из текстового ввода, распознаёт её и
 * возвращает один из классов-наследников Command, соответствующий введённой команде.
 * — Создайте data класс Person, который представляет собой запись о человеке. Этот класс должен содержать поля:
 * name – имя человека
 * phone – номер телефона
 * email – адрес электронной почты
 * — Добавьте новую команду show, которая выводит последнее значение, введённой с помощью команды add.
 * Для этого значение должно быть сохранено в переменную типа Person. Если на момент выполнения команды
 * show не было ничего введено, нужно вывести на экран сообщение “Not initialized”.
 * — Функция main должна выглядеть следующем образом. Для каждой команды от пользователя:
 * Читаем команду с помощью функции readCommand,
 * Выводим на экран получившийся экземпляр Command.
 * Если isValid для команды возвращает false, выводим help. Если true, обрабатываем команду внутри when.
 */

fun main() {

    var person: Person? = null

    while (true) {
        when (val command = readCommand()) {
            is CheckAddCommand -> {
                if (CheckAddCommand(command.type, command.name, command.value).isValid()) {
                    person = if (command.type == "addPhone") {
                        Person(command.name, command.value, "")
                    } else {
                        Person(command.name, "", command.value)
                    }
                } else {
                    println("Неправильно введены данные.")
                    println(HelpCommand.info())
                }
            }

            is ShowCommand -> println(ShowCommand.info(person))

            HelpCommand -> println(HelpCommand.info())

            ExitCommand -> {
                println(ExitCommand.info())
                break
            }

            InputError -> {
                println(InputError.info())
                println(HelpCommand.info())
            }

        }
    }
}

sealed interface Command {
    fun isValid(): Boolean
}


data object InputError : Command {
    fun info(): String {
        return "Некорректная команда. Выводим help для получения справки."
    }

    override fun isValid(): Boolean = true
}

data object ShowCommand : Command {
    fun info(person: Person?): Any {
        if (person != null)
            return "Последний добавленный контакт: $person"
        return "Not initialized"
    }

    override fun isValid(): Boolean = true
}

data object HelpCommand : Command {
    fun info(): String {
        return "Список доступных команд:\n" +
                "addPhone <Имя> <Номер телефона> - добавить номер телефона для контакта\n" +
                "addEmail <Имя> <Адрес электронной почты> - добавить адрес электронной почты для контакта\n" +
                "show - вывести последний добавленный контакт\n" +
                "help - вывести справку\n" +
                "exit - выход из программы\n"
    }

    override fun isValid(): Boolean {
        return true
    }
}

data object ExitCommand : Command {
    fun info(): String {
        return "Выход из программы\n"
    }

    override fun isValid(): Boolean = true
}

data class Person(val name: String, val phone: String, val email: String)

fun readCommand(): Command {
    val type: String
    val name: String
    val value: String


    print("Введите команду: ")
    val command = readlnOrNull()?.split(" ")
    type = command!![0]

    return when (type) {
        "addPhone" -> {
            name = command[1]
            value = command[2]
            CheckAddCommand(type, name, value)
        }

        "addEmail" -> {
            name = command[1]
            value = command[2]
            CheckAddCommand(type, name, value)
        }

        "show" -> ShowCommand
        "help" -> HelpCommand
        "exit" -> ExitCommand

        else -> {
            InputError
        }

    }
}

class CheckAddCommand(val type: String, val name: String, val value: String) : Command {
    override fun isValid(): Boolean {
        val phonePattern = """\+\d{12}$"""
        val emailPattern = """[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"""
        val namePattern = """[a-zA-Z]{2,}$"""

        if (type == "addPhone") {
            return when {
                name.matches(Regex(namePattern)) && value.matches(Regex(phonePattern)) -> {
                    true
                }

                else -> false
            }
        } else {
            return when {
                name.matches(Regex(namePattern)) && value.matches(Regex(emailPattern)) -> {
                    true
                }

                else -> false
            }
        }
    }
}



