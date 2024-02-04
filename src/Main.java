import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // Задача приложения найти и вывести в файл статистику сколько слов в тексте соответствует шаблонам
        // Шаблоны получаем из внешнего файла
        // Текст для анализа тоже получаем из внешнего файла
        // Результат записать в файл
        // при работе с файлами возможны сбои ==> нужно предусмотреть IO Exceptions

/*
При первом взгляде алгоритм видится таким:
- прочитать из файла шаблоны
- сделать из них мапу (key=string=шаблон, value=int=0-счетчик повторений)
- читаем из файла текст (сканером по словам), сравниваем слово со всеми шаблонами, при совпадении инкрементим счетчик
- когда текст закончится - открываем/создаем файл для результата и записываем туда содержимое мапы шаблонов, закрываем файл
 */

        String inputPatterns = readFileAsString(
                "D:\\TestPatterns.txt");

        Map<String, Integer> patternsMap = new LinkedHashMap<>();
        Scanner scan = new Scanner(inputPatterns);
        while (scan.hasNext()) {
            String p = scan.next(); //scanner automatically uses " " as a delimiter
            patternsMap.put(p, 0);
        }
        // Собрали шаблоны в мапу. Пока все value=0

        // Читаем слова из входного текста
        String inputText = readFileAsString(
                "D:\\TestText.txt");

        scan = new Scanner(inputText);
        while (scan.hasNext()) {
            String word = scan.next();
            for (HashMap.Entry<String, Integer> anyPattern : patternsMap.entrySet()) {
                String pattern = anyPattern.getKey();
                char[] charArray = pattern.toCharArray();
                if (charArray[0] == '"') {
                    if (checkString(word, pattern.substring(1, charArray.length - 1))) { // true если текущий шаблон обнаружен
                        int count = patternsMap.get(pattern) + 1; // увеличиваем счетчик текущего шаблона на 1
                        patternsMap.put(pattern, count); //записываем шаблон с новым значением счетчика
                    }
                } else // шаблон 1-го типа
                {
                    int numPairs = charArray.length / 2;
                    int checkIsOk = 0;
                    for (int i = 0; i < numPairs; i++) {
                        if (checkChar(word, charArray[i * 2], charArray[1 + i * 2] - '0'))
                            checkIsOk++; // true если текущий шаблон обнаружен
                    }
                    if (checkIsOk == numPairs) { // Ok по всем условиям шаблона
                        int count = patternsMap.get(pattern) + 1; // увеличиваем счетчик текущего шаблона на 1
                        patternsMap.put(pattern, count); //записываем шаблон с новым значением счетчика
                    }
                }
            }
        }
        System.out.println("Частотное распределение шаблонов в тексте :");
        System.out.println("шаблон \t количество ");
        try {
            FileWriter fWriter = new FileWriter(
                    "D:/testResult.txt");

            fWriter.write("шаблон \t количество \n"); // "шапка"

            for (HashMap.Entry<String, Integer> anyPattern : patternsMap.entrySet()) {
                fWriter.write(anyPattern.getKey() + "\t " + anyPattern.getValue() + "\n");
                System.out.println("  " + anyPattern.getKey() + "\t " + anyPattern.getValue());
            }
            fWriter.close();
            System.out.println("Запись информации в файл прошла успешно");
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }
    // проверяем наличие подстроки в слове
    public static boolean checkString(String word, String pattern) {
        return word.contains(pattern);
    }
    // проверяем наличие символа в слове нужное количество раз
    public static boolean checkChar(String word, char ch, int num) {
        return num == word.toLowerCase().chars().filter(c -> c == ch).count();// ? В ТЗ про Регистр ничего не сказано
    }
    public static String readFileAsString(String fileName)
            throws Exception {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}

