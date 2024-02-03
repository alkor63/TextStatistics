import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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

        String inputPatterns = "о2\n" +
                "т1о1\n" +
                "\"ми\"\n" +
                "ф2\n" +
                "11\n" +
                "\"йо\"\n" +
                "й1о1";

//                String inputText = removePunctuations(inputTextWithChar);//удаляем знаки пунктуации
        System.out.println("Шаблоны:\n" + inputPatterns);
        Map<String, Integer> patternsMap = new LinkedHashMap<>();
        Scanner scan = new Scanner(inputPatterns);
        while (scan.hasNext()) {
            String p = scan.next(); //scanner automatically uses " " as a delimiter
            patternsMap.put(p, 0);
        }
        // Собрали шаблоны в мапу. Пока все value=0
        System.out.println("Частотное распределение шаблонов в тексте :");
        System.out.println("шаблон \t количество ");
        for (HashMap.Entry<String, Integer> anyPattern : patternsMap.entrySet()) {
            System.out.println("   " + anyPattern.getKey() + "\t " + anyPattern.getValue());
        }

        // Читаем слова из входного текста
        String inputText = "Отец мой Андрей Петрович Гринев в молодости своей служил " +
                "при графе Минихе и вышел в отставку премьер-майором в 17.. году.";
        scan = new Scanner(inputText);
        while (scan.hasNext()) {
            String word = scan.next(); //scanner automatically uses " " as a delimiter
            System.out.println("current word = "+word);
            for (HashMap.Entry<String, Integer> anyPattern : patternsMap.entrySet()) {
                String pattern = anyPattern.getKey();
                char[] charArray = pattern.toCharArray();
                if (charArray[0] == '"') {
                    if (checkString(word, pattern.substring(1,charArray.length-1))) { // true если текущий шаблон обнаружен
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
        for (HashMap.Entry<String, Integer> anyPattern : patternsMap.entrySet()) {
            System.out.println("   " + anyPattern.getKey() + "\t " + anyPattern.getValue());
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

            public static boolean nullString(String s) {
                return (s == null || s.isEmpty() || s.isBlank());
            }
        }

