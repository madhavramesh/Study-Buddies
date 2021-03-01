# cs0320 Term Project 2021

**Team Members:** 

Madhav Ramesh, Richard Tang, Jack Cheng, Akash Singirikonda

**Team Strengths and Weaknesses:** 

**Strengths**  
* Madhav Ramesh
  - Sleep schedule aligns with others
  - Strong in extensibility and generics
* Richard Tang
  - Sleep schedule aligns with others
  - Good at planning, scheduling, and coding fast
* Jack Cheng
  * Sleep schedule aligns with others
  * Crushing hidden bugs
  * Semi-stable supply of caffeine
  * Likes algorithms
* Akash Singirikonda
  - Sleep schedule aligns with others
  - Good at writing test cases and project design

**Weaknesses**
* Madhav Ramesh
  - From cs19 (like the other group members - lack of diversity) 
  - 
* Richard Tang
  - Also from cs19
  - 
* Jack Cheng
  * Also from cs19
  * Limited experience in Java
  * Hates thinking about generics
* Akash Singirikonda
  * Also from cs19

**Project Idea(s):** _Fill this in with three unique ideas! (Due by March 1)_
### Idea 1 - Shiritori (Word Game) 
Requirements:
- It is attempting to solve the lack of a more complex shiritori game online

Features:
- Choose which place to “end” with (ie the first letter, second letter, etc.) so it’s not always the end
  - Most shiritori games always take the last letter as the beginning letter of the next word. Our version will allow the user to choose which letter is the next "beginning" place.
- Choose local, online, or computer opponent modes
  - This way, users can play with their friends, random people, or practice against a computer with variable difficulty settings.
- Option to choose to end with either letter, syllable, group of letters, or phonetic sound
  - This is where the main algorithmic complexity will probably come in. Most shiritori games are based on letters. However, in reality letters do not all have the same sound. We would like to implement an algorithm that can also check if a group of letters likely has the same phonetic sound. This way, the game resembles the real-life version more. This will probably be the most challenging part of the project.

### Idea 2 - Automated buying and reselling of sneakers 
Requirements:
- It is attempting to solve the lack of money by college students
- To increase the liquidity and efficiency of the sneaker market 

Features:
- Allows users to choose a maximum buy-in price
  - This is to accomodate the user's budget. 
- Allows users to choose a time period limit of reselling
  - Some people may need to make cash faster than others, albeit maybe at a lower profit level. This feature will allow users to choose a time period that they wish to resell within and the algorithm will make judgements on if a better price to resell will probably appear within the deadline. 
- Allows users to adjust risk and volatility levels
  - Allows the user to adjust the riskiness of the algorithm in the sense that the riskier it is, the more likely the algorithm is going to "hold" for a better resell value or buy more volatile shoes. This will probably be where the main algorithmic complexity lies and the most challenging part for the future.

### Idea 3 - Budget and find food on a trip

Requirements: 
- Help users find the best food along their trip within their budget

Features:
- Choose a budget
  - This can either be a budget for the total trip or a price average for each meal. The difference is that if the budget is set for the total trip, maybe a splurge restaurant/food option will be recommended. If the budget is set for price-on-average, all meals will be around the same price.
- Choose restaurant ratings
  - This will allow the user to choose a minimum restaurant rating based on a review website.
- Choose how far restaurants can be from the trip path
  - Some people may be more willing to go off their trip path to find food than others; this option will allow users to manage that option.
- Choose restaurants based off their instagram 
  - An algorithm that looks at the instagram profile of each picture and judges their posts by the vibrancy, amount of user comments and likes, follower count, and/or other metrics to determine how worthy a restaurant is of patronage. 

**Mentor TA:** _Put your mentor TA's name and email here once you're assigned one!_

## Meetings
_On your first meeting with your mentor TA, you should plan dates for at least the following meetings:_

**Specs, Mockup, and Design Meeting:** _(Schedule for on or before March 15)_

**4-Way Checkpoint:** _(Schedule for on or before April 5)_

**Adversary Checkpoint:** _(Schedule for on or before April 12 once you are assigned an adversary TA)_

## How to Build and Run
_A necessary part of any README!_
