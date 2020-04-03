# CoDoc19 🤖

CoDoc19 is a telegram bot which helps to let you know more about COVID-19. 

## Install ⚙️

The telegram handler is [@CoDoc19Bot](https://t.me/CoDoc19Bot). So just go to telegram app and start interacting with it :man_technologist:

## Features / Commands

To start interacting with the bot 🔰
```
/start
```
After entering this command you will get the available commands

### Corona Test 🌡

To check the likelihood of having COVID 19 📋

```
/test
```
<p align="center">
<img src="https://raw.githubusercontent.com/teamxenox/CoDoc19/master/screenshots/test.jpg" height="600"/>
</p>

With this test you can quickly check the likelihood of having Covid-19. This test is regularly updated and based on the recommendations of certified health organizations. It is purely intended to provide guidance and is not an official diagnosis.

### QA 👨‍⚕️

Provide trustworthy answers to questions about COVID-19 via NLP

<p align="center">
<img src="https://raw.githubusercontent.com/teamxenox/CoDoc19/master/screenshots/qa.jpg" height="600"/>
</p>

People have many questions about COVID-19, answers are scattered on different websites,
finding the right answers takes a lot of time and trustworthiness of answers is hard to judge. So we created a `QA` feature.
The model is currently in it's learning phase. This may result in matching of wrong question. 
I believe this will be improved in time.


### Quiz 🦉

To check if you're taking the correct protective measures against COVID-19.

```
/quiz
```

<p align="center">
<img src="https://raw.githubusercontent.com/teamxenox/CoDoc19/master/screenshots/quiz.jpg" height="600"/>
</p>

Several questions with options. You have to choose the right answer to ensure you have been good in taking measures :fist_left:

4. To get current statistics of death, cases, recoveries etc. :chart_with_upwards_trend:

### Statistics 📊

```
/stats
```

To get statistics related to death, active cases and recovered cases. Both global level and country wise.

#### Global 🌍

To get global statistics, you just have to pass the `/stats` command

<p align="center">
<img src="https://raw.githubusercontent.com/teamxenox/CoDoc19/master/screenshots/stats.jpg" height="600"/>
</p>

#### Country-wise 🗺
To get country-wise statistics, pass countrname with `/stats` command. eg `/stats India`
<p align="center">
<img src="https://raw.githubusercontent.com/teamxenox/CoDoc19/master/screenshots/stats_countries.jpg" height="600"/>
</p>

**PRO TIP** : You can directly pass **country name** or **country code** without `/stats`. eg try `India` 😉

### Charts 📉

You can also generate **charts** for both global and country stats 😎. 
Simply click the chart button to generate a chart.

<p align="center">
<img src="https://raw.githubusercontent.com/teamxenox/CoDoc19/master/screenshots/charts.jpg" height="600"/>
</p>

## TODO ✍️

- [ ] Build web interface 
- [ ] Build facebook bot


## Contributing 🤝

1. 🍴 Fork this repo!
2. **HACK AWAY!** 🔨🔨🔨
3. 🔃 Create a new pull request.

Feel free to contribute to this project and treat it like your own. 😊

## Contributors 🕺

- [theapache64 🚁](https://github.com/theapache64)
- [pavanjadhaw 🍀](https://github.com/pavanjadhaw)
- [shishirjha :cyclone:](https://github.com/shishirjha)

## Thanks to 🤗

- [CSSEGISandData/COVID-19](https://github.com/CSSEGISandData/COVID-19)
- [deepset-ai/COVID-QA](https://github.com/deepset-ai/COVID-QA)
- [CoronaTest](https://coronatest.live)
- [Roylab Stats](https://www.youtube.com/watch?v=qgylp3Td1Bw)
- [XChart](https://knowm.org/open-source/xchart/)
- [Telegram Bot API](https://core.telegram.org/bots/api)


## License 📄

[Apache 2.0](https://github.com/teamxenox/codoc19/blob/master/LICENSE)
