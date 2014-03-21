package org.checkerframework.framework.base;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;

import javacutils.Pair;

import org.checkerframework.framework.base.QualifiedTypeMirror;
import org.checkerframework.framework.base.QualifiedTypeMirror.QualifiedDeclaredType;
import org.checkerframework.framework.base.QualifiedTypeMirror.QualifiedExecutableType;
import org.checkerframework.framework.base.QualifiedTypeMirror.QualifiedTypeVariable;

/**
 * Used to compute the qualified type of a {@link Tree} or {@link Element}.
 * Also includes hooks that can be overridden to modify typechecking behavior
 * in type system-specific ways.
 */
public interface QualifiedTypeFactory<Q> {
    /** Gets the qualified type of an {@link Element}. */
    QualifiedTypeMirror<Q> getQualifiedType(Element element);
    /** Gets the qualified type of an AST node. */
    QualifiedTypeMirror<Q> getQualifiedType(Tree tree);
    /** Gets the qualified type from a type in {@link Tree} form. */
    QualifiedTypeMirror<Q> getQualifiedTypeFromTypeTree(Tree typeTree);

    /** Gets the qualified types of the bounds of a type parameter, identified
     * by its {@link Element}. */
    QualifiedTypeParameterBounds<Q> getQualifiedTypeParameterBounds(Element element);

    /** Gets the {@link QualifierHierarchy} used with this type system. */
    QualifierHierarchy<Q> getQualifierHierarchy();
    /** Gets the {@link TypeHierarchy} used with this type system. */
    TypeHierarchy<Q> getTypeHierarchy();

    /**
     * Hook for customizing the behavior of <code>directSuperTypes</code>.
     *
     * @param subtype
     *      the target of the <code>directSuperTypes</code> call
     * @param supertypes
     *      the supertypes produced by the default
     *      <code>directSuperTypes</code> implementation
     * @return
     *      a copy of <code>supertypes</code> after applying checker-specific
     *      adjustments
     */
    List<QualifiedTypeMirror<Q>> postDirectSuperTypes(QualifiedTypeMirror<Q> subtype, List<? extends QualifiedTypeMirror<Q>> supertypes);

    /** Hook for customizing the behavior of <code>asMemberOf</code>.
     *
     * @param memberType
     *      the type of the element being accessed, according to the default
     *      <code>asMemberOf</code> implementation
     * @param receiverType
     *      the type of the object instance whose element is being accessed
     * @param memberElement
     *      the element being accessed
     * @return
     *      a copy of <code>memberType</code> after applying checker-specific
     *      adjustments
     */
    QualifiedTypeMirror<Q> postAsMemberOf(QualifiedTypeMirror<Q> memberType, QualifiedTypeMirror<Q> receiverType, Element memberElement);

    /**
     * Hook for customizing the instantiation behavior for generics.
     *
     * @param type
     *      an instantiation of a generic type
     * @param element
     *      the {@link Element} corresponding to <code>type</code>
     * @return
     *      a copy of <code>type.getTypeArguments()</code> after applying
     *      checker-specific adjustments
     */
    List<QualifiedTypeVariable<Q>> typeVariablesFromUse(QualifiedDeclaredType<Q> type, TypeElement element);

    /** Hook for customizing type parameter inference for methods.
     *
     * @param tree
     *      the AST node for the method call
     * @return
     *      the type of the called method with all parameters instantiated, and
     *      a list of the type used to instantiate each parameter
     */
    Pair<QualifiedExecutableType<Q>, List<QualifiedTypeMirror<Q>>> methodFromUse(MethodInvocationTree tree);

    /**
     * Hook for customizing type parameter inference for constructors.
     *
     * @param tree
     *      the AST node for the constructor call
     * @return
     *      the type of the called constructor with all parameters
     *      instantiated, and a list of the type used to instantiate each
     *      parameter
     */
    Pair<QualifiedExecutableType<Q>, List<QualifiedTypeMirror<Q>>> constructorFromUse(NewClassTree tree);
}
