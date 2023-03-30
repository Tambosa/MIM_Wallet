# MIM_Wallet

![playstore](https://user-images.githubusercontent.com/73552496/228832852-ba2a0ffd-8b0d-4573-9b59-1dda5f985aef.png)

Иконка для приложения была сгенерирована при помощи Midjourney

## Приложение для просмотра цены на криптовалюты

### Features

- Хранит список имеющихся крипто валют пользователя.
- Показывает цены
- Изменения цены за фиксированный промежуток времени
- Круговая диаграмма
- Динамическая смена темы день/ночь
- Оповещения по расписанию

### Демонстрация экрана

Запуск приложения

<details>
  <summary>Fun fact!</summary>
  
  Несущественный баг меняет цвет диаграммы  при каждой рекомпозиции, мне понравилось это поведение и я решил временно оставить это как фичу.
 
</details>

https://user-images.githubusercontent.com/73552496/228832076-eb972d6a-0e69-47a9-9e43-b7bd4a8f5344.mp4

Смена темы с тёмной на светлую

https://user-images.githubusercontent.com/73552496/228832142-7a6e1d5f-2a20-42aa-84c0-4885f60de085.mp4

Удаление/добавление

https://user-images.githubusercontent.com/73552496/228832305-ce456e97-21eb-43d8-a103-27feffc63e97.mp4

Экран найстройки уведомлений

https://user-images.githubusercontent.com/73552496/228832389-e434e405-da47-487a-bd36-8fa5e566abc2.mp4

Пример оповщения 

![notification example](https://user-images.githubusercontent.com/73552496/226554463-cfd27c21-fdc6-4b27-b471-41d33f8656c0.jpeg)


### Techs

Для реализации проекта использовались следующие технологии
- MVVM + LiveData
- Retrofit
- Room
- DaggerHilt
- Coroutines
- Jetpack Compose

<details>
  <summary>Старый ui на xml + viewBinding</summary>
  
      https://user-images.githubusercontent.com/73552496/215429488-061e243e-ce79-43cf-b683-f1dc2fbd323f.mp4
  
</details>
