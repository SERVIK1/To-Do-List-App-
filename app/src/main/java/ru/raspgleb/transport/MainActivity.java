package ru.raspgleb.transport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    // Список задач
    private List<String> my_list = new ArrayList<>();
    // Список выбранных задач
    private List<String> selected = new ArrayList<String>();
    // Список удалённых задач для восстановления
    private List<String> deleted = new ArrayList<String>();
    // Список визуализация в интерфейсе
    ListView final_text;
    // Адаптер ListView
    ArrayAdapter<String> adapter;

    /**
     * @param text - сообщение пользователю
     */
    public void showInfo(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }

    /**
     * @param menu меню в отдельном окне
     * @return создание меню
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);


        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @param item элемент из меню
     * @return выбранный элемент
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Нужная кнопка в меню
        int id = item.getItemId();
        switch (id){
            //Удаление выбранных элементов
            case R.id.action1:
                if(selected.size()>0){
                    //Диалоговое окно с подтверждение от пользователя на удаление
                    new AlertDialog.Builder(MainActivity.this)
                            //.setIcon(R.mipmap.ic_x.png)
                            // Заголовок
                            .setTitle("УДАЛИТЬ ЭЛЕМЕНТ")
                            // Сообщение
                            .setMessage("Хотите удалить?")
                            // Подтверждение операции
                            .setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleted.clear();
                                    // Удаление элемента(ов) из адаптера и списка типа String
                                    for(i=0; i< selected.size();i++) {
                                        deleted.add(selected.get(i));
                                        adapter.remove(selected.get(i));
                                        my_list.remove(selected.get(i));
                                    }
                                    // Чистим выбранные позиции
                                    final_text.clearChoices();
                                    selected.clear();
                                    // Обновляем после внесения изменений
                                    adapter.notifyDataSetChanged();

                                }
                            })
                            // Отмена операции
                            .setNegativeButton("Нет", null)
                            .show();
                    return true;
                }
                else{
                    showInfo("Нет выделенных элементов");
                    return true;
                }

            case R.id.action2:
                if(selected.size()>0){
                    List<String> copy_of_list = new ArrayList<>();
                    List<String> copy_of_selected = new ArrayList<>();
                    for(String w:selected){
                        copy_of_selected.add(w);
                    }
                    for(String i:copy_of_selected){
                        copy_of_list.add(i);
                    }
                    for(String q:my_list){
                        if(!copy_of_list.contains(q)){
                            copy_of_list.add(q);
                        }
                    }
                    my_list.clear();
                    for(String p:copy_of_list){
                        my_list.add(p);
                    }
                    final_text.clearChoices();
                    for(String z: my_list){
                        if(copy_of_selected.contains(z)){
                            final_text.performItemClick(
                                    final_text.getAdapter().getView(my_list.indexOf(z), null, null),
                                    my_list.indexOf(z),
                                    final_text.getAdapter().getItemId(my_list.indexOf(z)));
                        }
                    }
                    selected.clear();
                    for(String r:copy_of_selected){
                        selected.add(r);
                    }
                    copy_of_selected.clear();
                    copy_of_list.clear();
                    adapter.notifyDataSetChanged();

                    return true;
                }
                else{
                    showInfo("Нет выделенных элементов");
                    return true;
                }
            case R.id.action3:
                if(deleted.size()>0){
                    new AlertDialog.Builder(MainActivity.this)
                            //.setIcon(R.mipmap.ic_x.png)
                            // Заголовок
                            .setTitle("ВОССТАНОВИТЬ УДАЛЁННЫЕ ЭЛЕМЕНТЫ")
                            // Сообщение
                            .setMessage("Хотите восстановить элементы с последнего удаления: " + "(" + String.valueOf(deleted.size())+" шт.)?")
                            // Подтверждение операции
                            .setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    for(i = 0;i<deleted.size();i++){
                                        my_list.add(deleted.get(i));
                                    }
                                    deleted.clear();
                                    adapter.notifyDataSetChanged();

                                }
                            })
                            // Отмена операции
                            .setNegativeButton("Нет", null)
                            .show();
                }
                else {
                    showInfo("Список недавно удалённых пуст");
                }


        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Кнопка добавления новых задач
        Button one = findViewById(R.id.button);
        // Инициализация объекта ListView
        final_text = findViewById(R.id.final_text);
        // Установка множественной выборки элементов
        final_text.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // Инициализация адаптера
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, my_list);
        // Установка адаптера в ListView
        final_text.setAdapter(adapter);

        /**
         * Нажатие на кнопку добавления новых задач
         */
        one.setOnClickListener((View view) -> {
                    //LayoutInflater берёт xml файл window и строит из него представление
                    LayoutInflater li = LayoutInflater.from(context);
                    //Создаём из layout файла View элемент, с помощью .inflate
                    View promptsView = li.inflate(R.layout.window, null);

                    //Создаем AlertDialog
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

                    //Настраиваем window.xml для нашего AlertDialog:
                    mDialogBuilder.setView(promptsView);

                    //Настраиваем отображение поля для ввода текста в открытом диалоге:
                    final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);

                    //Настраиваем сообщение в диалоговом окне:
                    mDialogBuilder
                            .setCancelable(false)
                            // Подтверждение на добавление новой задачи
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // Если список типа String не пуст
                                            if(!my_list.isEmpty()){
                                                // Параметр предотвращающий добавление повторяющихся задач
                                                boolean checker = false;
                                                // Перебор списка типа String
                                                for(String item_list:my_list){
                                                    // Проверяемая строка, полученная от пользователя
                                                    String checked = userInput.getText().toString();
                                                    if(checked.trim().equals(item_list.trim())){
                                                        showInfo("Эта задача уже в списке!");
                                                        checker = true;
                                                    }
                                                }
                                                // Совпадений не обнаружено
                                                if(!checker){
                                                    my_list.add(userInput.getText() + "\n");
                                                    adapter.notifyDataSetChanged();
                                                    checker = false;
                                                }

                                            }
                                            // Если список типа String был пуст
                                            else{
                                                //Вводим текст и отображаем в строке ввода на основном экране:
                                                my_list.add(userInput.getText() + "\n");
                                                adapter.notifyDataSetChanged();
                                            }


                                        }
                                    })
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    //Создаем AlertDialog:
                    AlertDialog alertDialog = mDialogBuilder.create();

                    //и отображаем его:
                    alertDialog.show();

                }

        );
        /**
         * Функция обработки выбранных элементов в ListView
         */
        final_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             *
             * @param adapter2 родитель
             * @param view выбранный элемент
             * @param position позиция
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> adapter2, View view, int position, long id) {
                // Получение элемента
                String task = adapter.getItem(position);
                // Если элемент выбран в ListView, то его добавляют в список повторяющихся типа String
                if(final_text.isItemChecked(position)) {
                    if (!selected.contains(task))
                        selected.add(task);
                }
                else {
                    selected.remove(task);
                }
            }
        });

    }

}
