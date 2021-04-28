package org.museautomation.runner.projects

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class IllegalProjectIdentifierException: Exception("Project ID contains restricted characters (/?<>\\:*|\")")