package ru.nsu.fit.m_polukhin;

import ru.nsu.fit.m_polukhin.exceptions.FileMissingException;
import ru.nsu.fit.m_polukhin.exceptions.PathFactoryException;
import ru.nsu.fit.m_polukhin.modules.DuCompoundFileType;
import ru.nsu.fit.m_polukhin.modules.DuFileType;
import ru.nsu.fit.m_polukhin.modules.MetaType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.*;

public class TreeFactory {
    private final List<MetaType<? extends DuFileType>> metaTypes;

    public TreeFactory(List<Class<? extends MetaType<? extends DuFileType>>> classList) throws PathFactoryException {
        this.metaTypes = new ArrayList<>();
        for (Class<? extends MetaType<? extends DuFileType>> clazz : classList) {
            try {
                this.metaTypes.add(clazz.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new PathFactoryException("Exception while instantiation of metaTypes");
            }
        }
    }

    DuFileType create(Path path, JduOptions jduOptions) throws PathFactoryException {
        var metaType = getMetaOf(path);
        if (metaType == null) {
            throw new PathFactoryException("Path" + path + "can't be recognized as any type");
        } else try {
            Class<? extends DuFileType> clazz = metaType.getFileType();
            // CR: you do not need reflection here. just add
            // CR: method createFileType(Path path) -> DuFileType into MetaType
            Constructor<? extends DuFileType> constructor = clazz.getConstructor(Path.class, JduOptions.class);
            return constructor.newInstance(path, jduOptions);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new PathFactoryException("Error creating instance of class " + metaType);
        }
    }

    public DuFileType buildTree(Path root, JduOptions options) throws PathFactoryException, FileMissingException {
        List<List<DuFileType>> layers = new ArrayList<>();
        // CR: can be not a dir
        var initialDir = create(root, options);
        layers.add(List.of(initialDir));
        for (int i = 1; i < options.depth() && layers.get(i-1).size() != 0; i++) {
           layers.add(initNextLayer(layers.get(i-1), options));
        }
        for (int i = layers.size() - 1; i >= 0; i--) {
            calculateLayer(layers.get(i));
        }
        return initialDir;
    }

    List<DuFileType> initNextLayer(List<DuFileType> layer, JduOptions options) throws FileMissingException, PathFactoryException {
        List<DuFileType> nextLayer = new ArrayList<>();
        for (var element: layer) {
            if(element instanceof DuCompoundFileType) {
                var childrenAsPaths = ((DuCompoundFileType) element).getChildrenAsPaths();
                List<DuFileType> childrenAsTypes = new ArrayList<>();
                // CR: what's the reason to return stream then?
                // CR: you can either change api (just return list) or handle paths in stream chain
                Iterator<Path> iterator = childrenAsPaths.iterator();
                while(iterator.hasNext()) {
                    Path path = iterator.next();
                    var duFile = create(path, options);
                    nextLayer.add(duFile);
                    childrenAsTypes.add(duFile);
                }
                ((DuCompoundFileType) element).setChildren(childrenAsTypes);
            }
        }
        return nextLayer;
    }

    void calculateLayer(List<DuFileType> layer) throws FileMissingException {
        for(var element : layer) {
            var meta = getMetaOf(element.path());
            assert meta != null;
            meta.calculateSize(element);
        }
    }

    MetaType<? extends DuFileType> getMetaOf(Path path) {
        for (var metaType : metaTypes) {
            if (metaType.isCompatible(path)) {
                return metaType;
            }
        }
        return null;
    }
}