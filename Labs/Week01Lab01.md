# Software Design and Architecture Week01 Lab 01 Worksheet

There are multiple activities each week, and you will probably not get everything done in the timetabled lab sessions; therefore, it is highly recommended that you complete the labs in your own time each week to avoid falling behind.

Completing the labs will get you ready for writing the assignment code.

**Advanced** Labs are optional, but completing the Advanced Labs will introduce you to more advanced techniques and improve your design skills.

These labs are all about getting your development environment set up for the rest of the module and preparing to write your assignment code.

## Get on GitHub and use Git
> ☑ The use of Git to manage source code is a fundamental skill. The 2021 Stack Overflow Survey suggested that over 93% of developers used Git for managing source code, and that percentage has grown even further since then as legacy projects switch to Git. Use of Git is a key skill for software engineers at any level.

From a command line prompt, run the command `git --version` to see if Git is already installed on your machine.

If it is not installed, follow the instructions at [Git - Installing Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) to install it.

Clone the code and labs repository for this module from https://github.com/SBakerMmu/6G5Z0059-2526-student.git using:

`git clone https://github.com/SBakerMmu/6G5Z0059-2526-student.git`.

### Get a GitHub Account (Advanced)

Go to https://github.com/ and sign up for a free account. You may already have done this, in which case log in.

Take the Introduction to GitHub tutorial from [GitHub Skills](https://skills.github.com/).

### Install a Git GUI (Advanced)
If you have a personal machine, you can use the GitHub Desktop software to work with Git using a graphical user interface (GUI). First, download and install GitHub Desktop from [GitHub Desktop](https://desktop.github.com/). Then, follow the tutorials at [GitHub Desktop Documentation](https://docs.github.com/en/desktop) to learn how to use it.

## Install the IntelliJ Integrated Development Environment (IDE)
> ☑ In industry, VS Code, Microsoft Visual Studio and JetBrains IntelliJ are the most widely used integrated development environments (IDEs). You have used Eclipse and VS Code in other modules, this will give you exposure to IntelliJ.
IntelliJ on University Machines

If you are using a University PC, then check if IntelliJ Community Edition is already installed. Not all PCs have this though - you will need a PC with specialist Science and Engineering software installed. The PCs in the Dalton building and lab rooms are specialist Science and Engineering PCs and should have IntelliJ installed. At time of writing, the Science and Engineering specialist PCs are in the following areas of the library.

- Ground floor of the library – room 33
- First floor of the library – room 118
- Second floor of the library – room 218
- Note that you will need to boot into Windows to access IntelliJ.

We will be using the Windows boot for all the labs in the module.
IntelliJ on Personal Machines

### IntelliJ on Personal Machines
If you have a personal laptop or home machine, then download and install the Community Edition from [JetBrains IntelliJ IDEA](https://www.jetbrains.com/idea/download/).

JetBrains offers two versions of IntelliJ: the Ultimate version, which requires a paid license, and the Community Edition, which is free to use. At the time of writing, both versions are available on the download page, so ensure you download and install the correct version.

Go to the Installation Guide page and follow the instructions for "Install IntelliJ IDEA - Standalone installation."

If you have a personal laptop or home machine, then download and install the Community Edition from
https://www.jetbrains.com/idea/download/

> ☠ JetBrains have two versions of IntelliJ, the Ultimate version is a paid for licence but the **Community Edition**, which is free to use. At time of writing, they are both on the download page, so endure you download and install the correct version.

Go to the Installation Guide page and follow the instructions for Install IntelliJ IDEA  - Standalone installation.

### Learn to use IntelliJ

Go to the [Getting Started](https://www.jetbrains.com/help/idea/getting-started.html) page to access the online tutorials to get you using IntelliJ productively.
> ☑ Following online tutorials to learn a new development tool is a valuable Software Engineering skill – it allows you to get started on projects by yourself which is a something employers will appreciate.

> ☠ Do not create Java projects on your One Drive, One Drive does not work well with software source code. Either use the W:\ Drive or (highly recommended) create the code on the C: drive and use Git to pull and push the source code between machines.

Follow these online tutorials to help you get using IntelliJ.

- Create your first Java application.
- Learn IDE features.
- Write the source code.
- Configure your project.


## Get a Coding Standard

> ☑ A coding standard is a set of rules about code style and naming. Having a consistent coding style makes your code easier to understand and navigate, both for yourself and for others.
>
> This significantly reduces the time and effort required for debugging, refactoring, and future enhancements.

Read the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

This is the standard we will be using for the Software Design and Architecture module—it is widely adopted in industry.

Pay particular attention to the sections on Source File Structure, Naming, and Programming Practice.

### Apply the Google Coding Style (Advanced)

Rework this code to apply the Google Java Style Guide.

```java
class badStyleClass {

    public int ADD1(int x, int y)
    {
        if (x < 6) return x + y;
        return 0;
    }

    public badStyleClass(){AVALUE = 1; bvalue = 1;}

    public int Add2(int x, int y)
    {
        int MYVariable=y;
        for(int i=0;i<MYVariable;i++){
            x += 1;
        }return x;
    }

    int AVALUE, bvalue = 0;
}

```

## Write Some Markdown

> ☑ Writing documents in Markdown has become common in software projects because it offers just enough formatting to be useful but requires almost no overhead to write. Markdown files are just text files, so they work well with source code and development tools like Git.

We will be using Markdown in the assignment. We can write Markdown using IntelliJ’s built-in Markdown editor, which provides a WYSIWYG experience for writing Markdown files.

Many text editors and online platforms (like GitHub or other project management platforms) support Markdown.

Markdown is very widely used in professional software projects. One reason is that keeping the documentation separate from the source code (for example as Word document) risks the two becoming out of synchronization. Writing the documentation using the same environment and using the same tools as your source code really helps make sure the documentation is up to date.

- Take the online 10-minute Tutorial from [https://commonmark.org/help/](https://commonmark.org/help/)
- Follow the instructions in [https://www.jetbrains.com/help/idea/markdown.html](https://www.jetbrains.com/help/idea/markdown.html) to create a Markdown file in IntelliJ.

### Example of Markdown syntax

```markdown
# Heading 1

## Heading 2

### Heading 3

*This text is italicized.*

**This text is bold.**

***This text is both bold and italicized.***

---

**This is an Unordered List**
- Item 1
- Item 2
  - Sub-item 1
    - Sub-item 2
- Item 3

---

**This is an ordered list**
1. First item
2. Second item
3. Third item

---

**Table**

| Fruit   | Color  | Price |
|---------|--------|-------|
| Apple   | Red    | 1.00  |
| Banana  | Yellow | 0.50  |

---

## A code example

```java
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}

```
Produces this output

# Heading 1

## Heading 2

### Heading 3

*This text is italicized.*

**This text is bold.**

***This text is both bold and italicized.***

---

**This is an Unordered List**
- Item 1
- Item 2
    - Sub-item 1
        - Sub-item 2
- Item 3

---

**This is an ordered list**
1. First item
2. Second item
3. Third item

---

**Table**

| Fruit   | Color  | Price |
|---------|--------|-------|
| Apple   | Red    | 1.00  |
| Banana  | Yellow | 0.50  |

---

## A code example

```java
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}
```
