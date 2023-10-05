## Code Review

Reviewed by: Yusi ZHONG, u7755061

Reviewing code written by: Hongjun XU u7733037

Component: <https://gitlab.cecs.anu.edu.au/u7751023/comp1110-ass2/-/blob/20e59f415397ffe35ae0824aa53c98353838387a/src/comp1110/ass2/gui/Game.java>


### Comments 

What are the best features of this code?
 - Well-structured and organized. The code follows object-oriented programming principles, particularly inheritance. 
   for example, the design of 'RugEntity' and its extension into 'DraggableRugEntity'. As we learned on lab, it increases the readability and maintainability of the code.

Is the code well-documented?
 - Yes, the code includes comments that cover important information, but more detailed comments explaining the logic and algorithm may enhance the documentation.
   for example, in the 'findNearest' method, it may take some time for others to understand the logic behind the calculations of coordinate and distance.
 - Using Javadoc-style comments for classes and method may improve readability. Proving the information on method purpose, parameters, return values and possible exceptions.

Is the program decomposition (class and method structure) appropriate? 
 - Yes, the classes and methods are appropriately decomposed, and demonstrate a clear separation of concerns and purposes. It focuses on specific responsibilities and functionalities.
 - The use of inheritance, just as mentioned before. promoting code reuse and extensibility.

Does it follow Java code conventions (for example, are methods and variables properly named), and is the style consistent throughout? 
 - Yes, the code follows java code conventions, with clear and meaningful naming, consistent use of camel case, enhance code readability.

If you suspect an error in the code, suggest a particular situation in which the program will not function correctly.
 - I did not find obvious error.





