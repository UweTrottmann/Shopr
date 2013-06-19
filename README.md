Shopr
=====

Suggests clothing shops to you based on Active Learning strategies. Just a research project!

The conversation-based active learning critiquing algorithm used is heavily based on

> Tweaking Critiquing, Lorraine McGinty and Barry Smyth, 2003.

> [PDF](http://www.researchgate.net/publication/230875729_Tweaking_Critiquing/file/9fcfd50f3fa6c955ff.pdf) (as of June 2013)

It differs by allowing negative critiques and uses these as an indicator of positive or negative progress. Why? Imagine a clothing item selection: you may not always say 'Hey, I like this color!', but also 'OMG, this color looks bad!'. It also helps limit the options for use on a smaller screen.


How to add a new attribute
--------------------------

1. Create a new class in shopr.algorithm.model extending GenericAttribute.
2. Add getter and setter in Attributes.
3. Add support in AdaptiveSelection.queryRevise().
4. Define end-user name in CritiqueActivity's ItemFeatureAdapter.getView().