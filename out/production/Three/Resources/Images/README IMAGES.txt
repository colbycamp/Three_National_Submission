Each image has different "States" of each type of object in itself
	(CAN BE CHANGED IF NOT ENJOYED)

0 - 10 characters
11-20 - walkable terrain
50-100 - special blocks
 > 100 - unwalkable terrain

Each Image can have an action. To do this all that is needed is
that the width of the image be changed while still keeping the
32 pixel by 32 pixel dimension for each additional image 
added. To make it animate simply change the WIDTH mean-
ing that adding to the right side of the image for the animation.

For entities, the format for the picture is as so,
up
right
down
left
walking up animation (will swap between up and walking pic)
walking right animation                        ||
walking down animation                      ||
walking left animation                           ||