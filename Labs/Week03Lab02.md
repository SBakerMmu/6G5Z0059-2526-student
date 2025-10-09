# Software Design and Architecture Week03 Lab 02 Worksheet

# Identify Candidate Classes for the Simple Frustration Game.

The assessment task is to write a simulation of a prototype physical board game called “Simple Frustration”. Full details are in the assessment brief in Moodle and there is a demo in Lecture 1 that shows an example simulation.

This lab exercise provides some time to identify some possible classes that model the key concepts and relationships within the game

• Entities: Things that represent important concepts or objects in the game

• Attributes: The “data” in Entities, either as primitives or (better) Value Objects

• Relationships between the classes

Some Kinds of classes

• Knowing: Knows and provides information (mostly holding data)

• Service Providing: Performs work on behalf of others (calculations, sending emails)

• Controlling: Makes decisions and delegates to other objects.

The Game also requires several variations. Identify the variations and how you might solve them using Strategies.

Use the lab time to get feedback from the tutors on your candidate classes.

# Implement the Bridge Pattern

The **More Ways of Handling Variation** chapter of the module textbook introduces the **Bridge** pattern.

Implement the example given that deals with Product and ProductPrinter.

All the code is provided in the module textbook, the objective of this advanced lab exercise to understanding how the pattern works.

Although usage of the Bridge pattern is not as common as the strategy pattern, it does solve some difficult design problems, so there is value in being aware of the pattern.
