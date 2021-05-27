#Generic Class Assembler 

---

An attempt to make bytecode modification easier, as something like dealing with reflection instead of confusing 
and unfamiliar file structure.

The steps goes like this:

>1. Bottom layer of code operating

All details of implementation will be exposed.

>2. First layer packaging

One or few opcodes will be translated into a low-grade language.

Class file management and class path searching function will be added in this stage.

>3. Second layer packaging

Several low-grade language objects will be translated into java source code, with code block structure

Will supports source code compilation in this stage as well