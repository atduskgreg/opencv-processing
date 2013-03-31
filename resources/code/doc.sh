# a shell script to create a java documentation 
# for a processing library. 
# 
# make changes to the variables below so they 
# fit the structure of your library

# the package name of your library
package=template;

# source folder location
src=../src;

# the destination folder of your documentation
dest=../documentation;


# compile the java documentation
javadoc -d $dest -stylesheetfile ./stylesheet.css -sourcepath ${src} ${package}
